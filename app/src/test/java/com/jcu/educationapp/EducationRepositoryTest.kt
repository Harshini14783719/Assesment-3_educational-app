package com.jcu.educationapp

import com.jcu.educationapp.data.local.OfflineQuestionDao
import com.jcu.educationapp.data.local.OfflineQuestionEntity
import com.jcu.educationapp.data.local.QuizResultDao
import com.jcu.educationapp.data.local.QuizResultEntity
import com.jcu.educationapp.data.remote.TriviaApiService
import com.jcu.educationapp.data.repository.EducationRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EducationRepositoryTest {

    private lateinit var repository: EducationRepositoryImpl
    private lateinit var fakeQuizDao: FakeQuizResultDao
    private lateinit var fakeQuestionDao: FakeOfflineQuestionDao

    @Before
    fun setUp() {
        fakeQuizDao = FakeQuizResultDao()
        fakeQuestionDao = FakeOfflineQuestionDao()
        repository = EducationRepositoryImpl(
            quizResultDao = fakeQuizDao,
            offlineQuestionDao = fakeQuestionDao
        )
    }

    @Test
    fun getQuestions_returnsFallbackOfflineQuestions_whenApiFails() = runTest {
        fakeQuestionDao.insertQuestions(
            listOf(
                OfflineQuestionEntity(
                    id = 1,
                    category = "Science",
                    difficulty = "Medium",
                    question = "What is ATP?",
                    correctAnswer = "Adenosine Triphosphate",
                    incorrectAnswersCsv = "ADP,DNA,RNA",
                    explanation = "ATP is energy."
                )
            )
        )

        val result = repository.getQuestions("Science", 1)
        assertTrue(result.isSuccess)
        val questions = result.getOrNull()
        assertNotNull(questions)
        assertTrue(questions!!.isNotEmpty())
        assertEquals("What is ATP?", questions[0].question)
    }

    @Test
    fun saveQuizResult_insertsIntoDao() = runTest {
        val entity = QuizResultEntity(
            category = "Science",
            score = 8,
            totalQuestions = 10,
            percentage = 80f,
            difficulty = "Medium",
            timeTakenSeconds = 120
        )

        val id = repository.saveQuizResult(entity)
        assertEquals(1L, id)

        val results = repository.getAllQuizResults().first()
        assertEquals(1, results.size)
        assertEquals("Science", results[0].category)
        assertEquals(80f, results[0].percentage)
    }
}

// Fake Implementations for Unit Testing
class FakeQuizResultDao : QuizResultDao {
    private val resultsList = mutableListOf<QuizResultEntity>()

    override suspend fun insertQuizResult(result: QuizResultEntity): Long {
        resultsList.add(result)
        return resultsList.size.toLong()
    }

    override fun getAllQuizResults() = flowOf(resultsList.toList())
    override fun getTotalQuizzesTaken() = flowOf(resultsList.size)
    override fun getAveragePercentage() = flowOf(resultsList.map { it.percentage }.average().toFloat())
    override fun getHighScore() = flowOf(resultsList.maxOfOrNull { it.score })
    override suspend fun deleteAllQuizResults() { resultsList.clear() }
}

class FakeOfflineQuestionDao : OfflineQuestionDao {
    private val questionsList = mutableListOf<OfflineQuestionEntity>()

    override suspend fun insertQuestions(questions: List<OfflineQuestionEntity>) {
        questionsList.addAll(questions)
    }

    override suspend fun getRandomQuestions(category: String, limit: Int): List<OfflineQuestionEntity> {
        return questionsList.take(limit)
    }

    override suspend fun getQuestionCount(): Int = questionsList.size
}
