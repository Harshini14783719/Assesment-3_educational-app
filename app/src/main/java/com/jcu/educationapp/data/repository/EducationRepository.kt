package com.jcu.educationapp.data.repository

import com.jcu.educationapp.data.local.QuizResultEntity
import kotlinx.coroutines.flow.Flow

interface EducationRepository {
    suspend fun getQuestions(category: String, amount: Int): Result<List<QuestionModel>>
    suspend fun saveQuizResult(result: QuizResultEntity): Long
    fun getAllQuizResults(): Flow<List<QuizResultEntity>>
    fun getTotalQuizzesTaken(): Flow<Int>
    fun getAveragePercentage(): Flow<Float?>
    fun getHighScore(): Flow<Int?>
    suspend fun clearHistory()
}
