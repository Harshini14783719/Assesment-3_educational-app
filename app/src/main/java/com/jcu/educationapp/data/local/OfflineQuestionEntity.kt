package com.jcu.educationapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_questions")
data class OfflineQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val difficulty: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswersCsv: String,
    val explanation: String
)
