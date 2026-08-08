package com.jcu.educationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.educationapp.data.repository.EducationRepository
import com.jcu.educationapp.util.PreferencesManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LandingUiState(
    val streakDays: Int = 3,
    val dailyTarget: Int = 10,
    val totalQuizzesTaken: Int = 0,
    val avgScorePercentage: Int = 0,
    val dailyFact: String = "Did you know? Mitochondria generate 90% of the chemical energy that human cells need to survive!"
)

class LandingViewModel(
    repository: EducationRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val uiState: StateFlow<LandingUiState> = repository.getAllQuizResults()
        .map { results ->
            val count = results.size
            val avg = if (results.isNotEmpty()) {
                results.map { it.percentage }.average().toInt()
            } else {
                0
            }
            LandingUiState(
                streakDays = preferencesManager.streakCount,
                dailyTarget = preferencesManager.dailyTargetQuestions,
                totalQuizzesTaken = count,
                avgScorePercentage = avg
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LandingUiState(
                streakDays = preferencesManager.streakCount,
                dailyTarget = preferencesManager.dailyTargetQuestions
            )
        )
}
