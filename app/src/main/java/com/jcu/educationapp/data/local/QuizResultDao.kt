package com.jcu.educationapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResultEntity): Long

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllQuizResults(): Flow<List<QuizResultEntity>>

    @Query("SELECT COUNT(*) FROM quiz_results")
    fun getTotalQuizzesTaken(): Flow<Int>

    @Query("SELECT AVG(percentage) FROM quiz_results")
    fun getAveragePercentage(): Flow<Float?>

    @Query("SELECT MAX(score) FROM quiz_results")
    fun getHighScore(): Flow<Int?>

    @Query("DELETE FROM quiz_results")
    suspend fun deleteAllQuizResults()
}
