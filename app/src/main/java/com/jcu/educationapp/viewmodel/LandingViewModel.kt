package com.jcu.educationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.educationapp.data.repository.EducationRepository
import com.jcu.educationapp.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LandingUiState(
    val streakDays: Int = 3,
    val dailyTarget: Int = 10,
    val totalQuizzesTaken: Int = 0,
    val avgScorePercentage: Int = 0,
    val dailyQuote: String = "The important thing is not to stop questioning. Curiosity has its own reason for existing.",
    val quoteAuthor: String = "Albert Einstein",
    val dailyFact: String = "Did you know? Mitochondria generate 90% of the chemical energy that human cells need to survive!"
)

class LandingViewModel(
    private val repository: EducationRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LandingUiState(
            streakDays = preferencesManager.streakCount,
            dailyTarget = preferencesManager.dailyTargetQuestions
        )
    )
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()

    init {
        loadDailyContent()
        observeStats()
    }

    private fun loadDailyContent() {
        viewModelScope.launch {
            val quoteResult = repository.getDailyQuote()
            val factResult = repository.getDailyFact()

            _uiState.update {
                it.copy(
                    dailyQuote = quoteResult.first,
                    quoteAuthor = quoteResult.second,
                    dailyFact = factResult
                )
            }
        }
    }

    private fun observeStats() {
        viewModelScope.launch {
            repository.getAllQuizResults().collect { results ->
                val count = results.size
                val avg = if (results.isNotEmpty()) results.map { it.percentage }.average().toInt() else 0

                _uiState.update {
                    it.copy(
                        totalQuizzesTaken = count,
                        avgScorePercentage = avg
                    )
                }
            }
        }
    }
}
