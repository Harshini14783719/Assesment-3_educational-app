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

data class SettingsUiState(
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val difficultyLevel: String = "Medium",
    val dailyTarget: Int = 10,
    val isResetDialogOpen: Boolean = false
)

class SettingsViewModel(
    private val repository: EducationRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            soundEnabled = preferencesManager.soundEnabled,
            vibrationEnabled = preferencesManager.vibrationEnabled,
            difficultyLevel = preferencesManager.difficultyLevel,
            dailyTarget = preferencesManager.dailyTargetQuestions
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleSound(enabled: Boolean) {
        preferencesManager.soundEnabled = enabled
        _uiState.update { it.copy(soundEnabled = enabled) }
    }

    fun toggleVibration(enabled: Boolean) {
        preferencesManager.vibrationEnabled = enabled
        _uiState.update { it.copy(vibrationEnabled = enabled) }
    }

    fun setDifficulty(difficulty: String) {
        preferencesManager.difficultyLevel = difficulty
        _uiState.update { it.copy(difficultyLevel = difficulty) }
    }

    fun setDailyTarget(target: Int) {
        preferencesManager.dailyTargetQuestions = target
        _uiState.update { it.copy(dailyTarget = target) }
    }

    fun showResetDialog(show: Boolean) {
        _uiState.update { it.copy(isResetDialogOpen = show) }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.clearHistory()
            showResetDialog(false)
        }
    }
}
