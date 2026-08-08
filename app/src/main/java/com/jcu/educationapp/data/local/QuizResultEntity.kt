package com.jcu.educationapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Float,
    val difficulty: String,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)
