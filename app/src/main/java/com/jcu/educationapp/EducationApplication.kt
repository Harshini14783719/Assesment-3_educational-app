package com.jcu.educationapp

import android.app.Application
import com.jcu.educationapp.data.local.AppDatabase
import com.jcu.educationapp.data.repository.EducationRepositoryImpl
import com.jcu.educationapp.util.PreferencesManager
import com.jcu.educationapp.util.SoundManager

class EducationApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var repository: EducationRepositoryImpl
        private set

    lateinit var preferencesManager: PreferencesManager
        private set

    lateinit var soundManager: SoundManager
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        preferencesManager = PreferencesManager(this)
        soundManager = SoundManager(this)
        repository = EducationRepositoryImpl(
            quizResultDao = database.quizResultDao(),
            offlineQuestionDao = database.offlineQuestionDao()
        )
    }
}
