package com.jcu.educationapp.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("brainspark_prefs", Context.MODE_PRIVATE)

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION, value).apply()

    var difficultyLevel: String
        get() = prefs.getString(KEY_DIFFICULTY, "Medium") ?: "Medium"
        set(value) = prefs.edit().putString(KEY_DIFFICULTY, value).apply()

    var dailyTargetQuestions: Int
        get() = prefs.getInt(KEY_DAILY_TARGET, 10)
        set(value) = prefs.edit().putInt(KEY_DAILY_TARGET, value).apply()

    var streakCount: Int
        get() = prefs.getInt(KEY_STREAK, 3)
        set(value) = prefs.edit().putInt(KEY_STREAK, value).apply()

    companion object {
        private const val KEY_SOUND = "sound_enabled"
        private const val KEY_VIBRATION = "vibration_enabled"
        private const val KEY_DIFFICULTY = "difficulty_level"
        private const val KEY_DAILY_TARGET = "daily_target"
        private const val KEY_STREAK = "streak_count"
    }
}
