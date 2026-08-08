package com.jcu.educationapp.data.repository

import android.os.Build
import android.text.Html
import com.jcu.educationapp.data.local.OfflineQuestionDao
import com.jcu.educationapp.data.local.OfflineQuestionEntity
import com.jcu.educationapp.data.local.QuizResultDao
import com.jcu.educationapp.data.local.QuizResultEntity
import com.jcu.educationapp.data.remote.TriviaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

class EducationRepositoryImpl(
    private val quizResultDao: QuizResultDao,
    private val offlineQuestionDao: OfflineQuestionDao,
    private val apiService: TriviaApiService = TriviaApiService.create()
) : EducationRepository {

    override suspend fun getQuestions(category: String, amount: Int): Result<List<QuestionModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val categoryId = when (category) {
                    "Science" -> 17
                    "Computers" -> 18
                    "Math" -> 19
                    else -> 17
                }

                val response = apiService.getQuestions(amount = amount, category = categoryId)
                if (response.isSuccessful && response.body() != null && response.body()!!.results.isNotEmpty()) {
                    val remoteQuestions = response.body()!!.results.map { dto ->
                        val options = (dto.incorrectAnswers + dto.correctAnswer).map { decodeHtml(it) }.shuffled()
                        QuestionModel(
                            id = UUID.randomUUID().toString(),
                            category = decodeHtml(dto.category),
                            difficulty = dto.difficulty.replaceFirstChar { it.uppercase() },
                            question = decodeHtml(dto.question),
                            options = options,
                            correctAnswer = decodeHtml(dto.correctAnswer),
                            explanation = "Correct answer is '${decodeHtml(dto.correctAnswer)}'. Category: ${decodeHtml(dto.category)}."
                        )
                    }
                    Result.success(remoteQuestions)
                } else {
                    // Fallback to offline Room database
                    fetchOfflineQuestions(category, amount)
                }
            } catch (e: Exception) {
                // Network failure -> Fallback to Room database
                fetchOfflineQuestions(category, amount)
            }
        }
    }

    private suspend fun fetchOfflineQuestions(category: String, amount: Int): Result<List<QuestionModel>> {
        val offlineEntities = offlineQuestionDao.getRandomQuestions(category, amount)
        if (offlineEntities.isNotEmpty()) {
            val models = offlineEntities.map { entity ->
                val incorrectList = entity.incorrectAnswersCsv.split(",").map { it.trim() }
                val options = (incorrectList + entity.correctAnswer).shuffled()
                QuestionModel(
                    id = entity.id.toString(),
                    category = entity.category,
                    difficulty = entity.difficulty,
                    question = entity.question,
                    options = options,
                    correctAnswer = entity.correctAnswer,
                    explanation = entity.explanation
                )
            }
            return Result.success(models)
        } else {
            // Guarantee fallback items even if db callback hasn't finished
            val hardcodedDefaults = getHardcodedFallbackQuestions(category)
            return Result.success(hardcodedDefaults.take(amount))
        }
    }

    override suspend fun saveQuizResult(result: QuizResultEntity): Long {
        return withContext(Dispatchers.IO) {
            quizResultDao.insertQuizResult(result)
        }
    }

    override fun getAllQuizResults(): Flow<List<QuizResultEntity>> = quizResultDao.getAllQuizResults()
    override fun getTotalQuizzesTaken(): Flow<Int> = quizResultDao.getTotalQuizzesTaken()
    override fun getAveragePercentage(): Flow<Float?> = quizResultDao.getAveragePercentage()
    override fun getHighScore(): Flow<Int?> = quizResultDao.getHighScore()

    override suspend fun clearHistory() {
        withContext(Dispatchers.IO) {
            quizResultDao.deleteAllQuizResults()
        }
    }

    private fun decodeHtml(text: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(text).toString()
        }
    }

    private fun getHardcodedFallbackQuestions(category: String): List<QuestionModel> {
        return listOf(
            QuestionModel(
                id = "fb1",
                category = "Science",
                difficulty = "Medium",
                question = "What is the primary power-producing organelle inside eukaryotic cells?",
                options = listOf("Mitochondria", "Ribosome", "Lysosome", "Golgi Apparatus").shuffled(),
                correctAnswer = "Mitochondria",
                explanation = "Mitochondria convert glucose into ATP energy for cellular processes."
            ),
            QuestionModel(
                id = "fb2",
                category = "Computers",
                difficulty = "Medium",
                question = "What does the abbreviation 'API' stand for in software development?",
                options = listOf("Application Programming Interface", "Automated Protocol Integration", "Applied Programming Index", "Access Process Indicator").shuffled(),
                correctAnswer = "Application Programming Interface",
                explanation = "An API defines clear contracts for building software applications."
            ),
            QuestionModel(
                id = "fb3",
                category = "Science",
                difficulty = "Easy",
                question = "Which chemical element has the atomic symbol 'Fe'?",
                options = listOf("Iron", "Fluorine", "Francium", "Gold").shuffled(),
                correctAnswer = "Iron",
                explanation = "'Fe' comes from the Latin word 'Ferrum'."
            ),
            QuestionModel(
                id = "fb4",
                category = "Computers",
                difficulty = "Easy",
                question = "Which layout component in Jetpack Compose lays out items vertically?",
                options = listOf("Column", "Row", "Box", "LazyGrid").shuffled(),
                correctAnswer = "Column",
                explanation = "Column arranges UI elements in a vertical stack."
            )
        )
    }
}
