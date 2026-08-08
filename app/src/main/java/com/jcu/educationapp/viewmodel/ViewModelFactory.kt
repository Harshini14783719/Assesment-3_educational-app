package com.jcu.educationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcu.educationapp.data.repository.EducationRepository
import com.jcu.educationapp.util.PreferencesManager
import com.jcu.educationapp.util.SoundManager

class ViewModelFactory(
    private val repository: EducationRepository,
    private val preferencesManager: PreferencesManager,
    private val soundManager: SoundManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LandingViewModel::class.java) -> {
                LandingViewModel(repository, preferencesManager) as T
            }
            modelClass.isAssignableFrom(QuizViewModel::class.java) -> {
                QuizViewModel(repository, preferencesManager, soundManager) as T
            }
            modelClass.isAssignableFrom(StatsViewModel::class.java) -> {
                StatsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository, preferencesManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
