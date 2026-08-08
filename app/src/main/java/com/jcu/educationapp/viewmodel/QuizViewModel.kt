package com.jcu.educationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.educationapp.data.local.QuizResultEntity
import com.jcu.educationapp.data.repository.EducationRepository
import com.jcu.educationapp.data.repository.QuestionModel
import com.jcu.educationapp.util.PreferencesManager
import com.jcu.educationapp.util.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface QuizUiState {
    data object Loading : QuizUiState
    data class Active(
        val questions: List<QuestionModel>,
        val currentIndex: Int,
        val selectedOption: String? = null,
        val isAnswerSubmitted: Boolean = false,
        val isCorrect: Boolean? = null,
        val currentScore: Int = 0,
        val timerSecondsRemaining: Int = 20,
        val category: String = "Science",
        val difficulty: String = "Medium"
    ) : QuizUiState

    data class Completed(
        val finalScore: Int,
        val totalQuestions: Int,
        val percentage: Float,
        val category: String,
        val timeTakenSeconds: Int
    ) : QuizUiState

    data class Error(val message: String) : QuizUiState
}

class QuizViewModel(
    private val repository: EducationRepository,
    private val preferencesManager: PreferencesManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var totalTimeElapsedSeconds = 0

    fun loadQuiz(category: String = "Science", amount: Int = 5) {
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            totalTimeElapsedSeconds = 0

            val difficulty = preferencesManager.difficultyLevel
            val result = repository.getQuestions(category, amount)
            result.onSuccess { questions ->
                if (questions.isNotEmpty()) {
                    _uiState.value = QuizUiState.Active(
                        questions = questions,
                        currentIndex = 0,
                        category = category,
                        difficulty = difficulty
                    )
                    startTimer()
                } else {
                    _uiState.value = QuizUiState.Error("No questions available for this category.")
                }
            }.onFailure { error ->
                _uiState.value = QuizUiState.Error(error.localizedMessage ?: "Failed to fetch quiz content.")
            }
        }
    }

    fun selectOption(option: String) {
        val currentState = _uiState.value as? QuizUiState.Active ?: return
        if (currentState.isAnswerSubmitted) return

        _uiState.update {
            (it as QuizUiState.Active).copy(selectedOption = option)
        }
    }

    fun submitAnswer() {
        val currentState = _uiState.value as? QuizUiState.Active ?: return
        if (currentState.selectedOption == null || currentState.isAnswerSubmitted) return

        timerJob?.cancel()
        val currentQuestion = currentState.questions[currentState.currentIndex]
        val isCorrect = currentState.selectedOption == currentQuestion.correctAnswer

        if (isCorrect) {
            soundManager.playCorrectSound(preferencesManager.soundEnabled)
        } else {
            soundManager.playIncorrectSound(preferencesManager.soundEnabled)
        }
        soundManager.triggerVibration(preferencesManager.vibrationEnabled)

        val newScore = if (isCorrect) currentState.currentScore + 1 else currentState.currentScore

        _uiState.update {
            currentState.copy(
                isAnswerSubmitted = true,
                isCorrect = isCorrect,
                currentScore = newScore
            )
        }
    }

    fun nextQuestion() {
        val currentState = _uiState.value as? QuizUiState.Active ?: return
        val nextIndex = currentState.currentIndex + 1

        if (nextIndex < currentState.questions.size) {
            _uiState.update {
                currentState.copy(
                    currentIndex = nextIndex,
                    selectedOption = null,
                    isAnswerSubmitted = false,
                    isCorrect = null,
                    timerSecondsRemaining = 20
                )
            }
            startTimer()
        } else {
            // Quiz Complete
            finishQuiz(currentState)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                totalTimeElapsedSeconds++
                val currentState = _uiState.value as? QuizUiState.Active ?: break

                if (currentState.timerSecondsRemaining > 1) {
                    _uiState.update {
                        currentState.copy(timerSecondsRemaining = currentState.timerSecondsRemaining - 1)
                    }
                } else {
                    // Time out for current question
                    _uiState.update {
                        currentState.copy(
                            timerSecondsRemaining = 0,
                            isAnswerSubmitted = true,
                            isCorrect = false
                        )
                    }
                    soundManager.playIncorrectSound(preferencesManager.soundEnabled)
                    soundManager.triggerVibration(preferencesManager.vibrationEnabled)
                    break
                }
            }
        }
    }

    private fun finishQuiz(activeState: QuizUiState.Active) {
        timerJob?.cancel()
        val total = activeState.questions.size
        val score = activeState.currentScore
        val percentage = (score.toFloat() / total) * 100f

        val resultEntity = QuizResultEntity(
            category = activeState.category,
            score = score,
            totalQuestions = total,
            percentage = percentage,
            difficulty = activeState.difficulty,
            timeTakenSeconds = totalTimeElapsedSeconds
        )

        viewModelScope.launch {
            repository.saveQuizResult(resultEntity)
            _uiState.value = QuizUiState.Completed(
                finalScore = score,
                totalQuestions = total,
                percentage = percentage,
                category = activeState.category,
                timeTakenSeconds = totalTimeElapsedSeconds
            )
        }
    }
}
