package com.jcu.educationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.jcu.educationapp.ui.navigation.NavGraph
import com.jcu.educationapp.ui.theme.BrainSparkTheme
import com.jcu.educationapp.viewmodel.LandingViewModel
import com.jcu.educationapp.viewmodel.QuizViewModel
import com.jcu.educationapp.viewmodel.SettingsViewModel
import com.jcu.educationapp.viewmodel.StatsViewModel
import com.jcu.educationapp.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as EducationApplication
        val factory = ViewModelFactory(
            repository = app.repository,
            preferencesManager = app.preferencesManager,
            soundManager = app.soundManager
        )

        val landingViewModel = ViewModelProvider(this, factory)[LandingViewModel::class.java]
        val quizViewModel = ViewModelProvider(this, factory)[QuizViewModel::class.java]
        val statsViewModel = ViewModelProvider(this, factory)[StatsViewModel::class.java]
        val settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        setContent {
            BrainSparkTheme {
                NavGraph(
                    landingViewModel = landingViewModel,
                    quizViewModel = quizViewModel,
                    statsViewModel = statsViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
