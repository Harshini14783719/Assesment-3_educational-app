package com.jcu.educationapp.data.repository

data class QuestionModel(
    val id: String,
    val category: String,
    val difficulty: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)
