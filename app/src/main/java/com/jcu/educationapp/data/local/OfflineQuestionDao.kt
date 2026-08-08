package com.jcu.educationapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OfflineQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<OfflineQuestionEntity>)

    @Query("SELECT * FROM offline_questions WHERE category = :category OR :category = 'All' ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestions(category: String, limit: Int): List<OfflineQuestionEntity>

    @Query("SELECT COUNT(*) FROM offline_questions")
    suspend fun getQuestionCount(): Int
}
