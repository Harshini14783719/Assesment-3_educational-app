package com.jcu.educationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.educationapp.data.local.QuizResultEntity
import com.jcu.educationapp.data.repository.EducationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class StatsUiState(
    val results: List<QuizResultEntity> = emptyList(),
    val totalQuizzes: Int = 0,
    val averagePercentage: Float = 0f,
    val highScore: Int = 0,
    val bestCategory: String = "Science"
)

class StatsViewModel(
    private val repository: EducationRepository
) : ViewModel() {

    val uiState: StateFlow<StatsUiState> = repository.getAllQuizResults()
        .map { results ->
            val total = results.size
            val avg = if (results.isNotEmpty()) results.map { it.percentage }.average().toFloat() else 0f
            val maxScore = results.maxOfOrNull { it.score } ?: 0
            val bestCat = results.groupBy { it.category }
                .maxByOrNull { entry -> entry.value.sumOf { it.score } }
                ?.key ?: "Science"

            StatsUiState(
                results = results,
                totalQuizzes = total,
                averagePercentage = avg,
                highScore = maxScore,
                bestCategory = bestCat
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StatsUiState()
        )

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
