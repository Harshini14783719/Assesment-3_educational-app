package com.jcu.educationapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [QuizResultEntity::class, OfflineQuestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quizResultDao(): QuizResultDao
    abstract fun offlineQuestionDao(): OfflineQuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "brainspark_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialQuestions(database.offlineQuestionDao())
                }
            }
        }

        private suspend fun populateInitialQuestions(dao: OfflineQuestionDao) {
            val initialQuestions = listOf(
                OfflineQuestionEntity(
                    category = "Science",
                    difficulty = "Medium",
                    question = "What is the primary power-producing organelle inside eukaryotic cells?",
                    correctAnswer = "Mitochondria",
                    incorrectAnswersCsv = "Ribosome,Lysosome,Golgi Apparatus",
                    explanation = "Mitochondria convert glucose and oxygen into ATP, the cellular energy currency."
                ),
                OfflineQuestionEntity(
                    category = "Science",
                    difficulty = "Easy",
                    question = "Which chemical element has the atomic symbol 'Fe'?",
                    correctAnswer = "Iron",
                    incorrectAnswersCsv = "Fluorine,Francium,Gold",
                    explanation = "Fe comes from the Latin word 'Ferrum' which means Iron."
                ),
                OfflineQuestionEntity(
                    category = "Computers",
                    difficulty = "Medium",
                    question = "What does the abbreviation 'API' stand for in software engineering?",
                    correctAnswer = "Application Programming Interface",
                    incorrectAnswersCsv = "Automated Protocol Integration,Applied Programming Index,Access Process Indicator",
                    explanation = "An API defines interactions between multiple software applications or components."
                ),
                OfflineQuestionEntity(
                    category = "Science",
                    difficulty = "Hard",
                    question = "What is the speed of light in vacuum to the nearest million meters per second?",
                    correctAnswer = "300 Million m/s",
                    incorrectAnswersCsv = "150 Million m/s,450 Million m/s,600 Million m/s",
                    explanation = "Light travels in vacuum at approximately 299,792,458 meters per second (approx 3.00 x 10^8 m/s)."
                ),
                OfflineQuestionEntity(
                    category = "Computers",
                    difficulty = "Easy",
                    question = "In Jetpack Compose, which composable is used to lay out items vertically?",
                    correctAnswer = "Column",
                    incorrectAnswersCsv = "Row,Box,LazyGrid",
                    explanation = "Column arranges its layout children in a vertical sequence."
                ),
                OfflineQuestionEntity(
                    category = "Science",
                    difficulty = "Medium",
                    question = "Which planet in our solar system has the highest surface temperature?",
                    correctAnswer = "Venus",
                    incorrectAnswersCsv = "Mercury,Mars,Jupiter",
                    explanation = "Venus has a thick greenhouse gas atmosphere that traps heat, making it hotter than Mercury."
                )
            )
            dao.insertQuestions(initialQuestions)
        }
    }
}
