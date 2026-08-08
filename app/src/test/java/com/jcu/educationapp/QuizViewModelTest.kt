package com.jcu.educationapp

import com.jcu.educationapp.data.local.QuizResultEntity
import com.jcu.educationapp.data.repository.EducationRepository
import com.jcu.educationapp.data.repository.QuestionModel
import com.jcu.educationapp.viewmodel.QuizUiState
import com.jcu.educationapp.viewmodel.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeEducationRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeEducationRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadQuiz_populatesActiveStateWithQuestions() = runTest {
        val viewModel = QuizViewModel(fakeRepository, FakePreferencesManager(), FakeSoundManager())
        viewModel.loadQuiz("Science", 2)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is QuizUiState.Active)
        val activeState = state as QuizUiState.Active
        assertEquals(2, activeState.questions.size)
        assertEquals("Science", activeState.category)
    }

    @Test
    fun selectOption_updatesSelectedOptionInState() = runTest {
        val viewModel = QuizViewModel(fakeRepository, FakePreferencesManager(), FakeSoundManager())
        viewModel.loadQuiz("Science", 2)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.selectOption("Option A")
        val activeState = viewModel.uiState.value as QuizUiState.Active
        assertEquals("Option A", activeState.selectedOption)
    }
}

class FakeEducationRepository : EducationRepository {
    private val fakeQuestions = listOf(
        QuestionModel("1", "Science", "Medium", "What is H2O?", listOf("Water", "Gas", "Acid"), "Water", "H2O is water."),
        QuestionModel("2", "Science", "Medium", "What is CO2?", listOf("Carbon Dioxide", "Oxygen", "Nitrogen"), "Carbon Dioxide", "CO2 is carbon dioxide.")
    )

    override suspend fun getQuestions(category: String, amount: Int): Result<List<QuestionModel>> {
        return Result.success(fakeQuestions.take(amount))
    }

    override suspend fun saveQuizResult(result: QuizResultEntity): Long = 1L
    override fun getAllQuizResults(): Flow<List<QuizResultEntity>> = flowOf(emptyList())
    override fun getTotalQuizzesTaken(): Flow<Int> = flowOf(0)
    override fun getAveragePercentage(): Flow<Float?> = flowOf(0f)
    override fun getHighScore(): Flow<Int?> = flowOf(0)
    override suspend fun clearHistory() {}
}

class FakePreferencesManager : com.jcu.educationapp.util.PreferencesManager(FakeContext()) {
    override var soundEnabled: Boolean = true
    override var vibrationEnabled: Boolean = true
    override var difficultyLevel: String = "Medium"
    override var dailyTargetQuestions: Int = 10
    override var streakCount: Int = 3
}

class FakeSoundManager : com.jcu.educationapp.util.SoundManager(FakeContext())

class FakeContext : android.content.ContextWrapper(null) {
    override fun getSharedPreferences(name: String?, mode: Int): android.content.SharedPreferences {
        return FakeSharedPreferences()
    }
}

class FakeSharedPreferences : android.content.SharedPreferences {
    override fun getAll(): MutableMap<String, *> = mutableMapOf<String, Any>()
    override fun getString(key: String?, defValue: String?): String? = defValue
    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? = defValues
    override fun getInt(key: String?, defValue: Int): Int = defValue
    override fun getLong(key: String?, defValue: Long): Long = defValue
    override fun getFloat(key: String?, defValue: Float): Float = defValue
    override fun getBoolean(key: String?, defValue: Boolean): Boolean = defValue
    override fun contains(key: String?): Boolean = false
    override fun edit(): android.content.SharedPreferences.Editor = FakeEditor()
    override fun registerOnSharedPreferenceChangeListener(listener: android.content.SharedPreferences.OnSharedPreferenceChangeListener?) {}
    override fun unregisterOnSharedPreferenceChangeListener(listener: android.content.SharedPreferences.OnSharedPreferenceChangeListener?) {}
}

class FakeEditor : android.content.SharedPreferences.Editor {
    override fun putString(key: String?, value: String?): android.content.SharedPreferences.Editor = this
    override fun putStringSet(key: String?, values: MutableSet<String>?): android.content.SharedPreferences.Editor = this
    override fun putInt(key: String?, value: Int): android.content.SharedPreferences.Editor = this
    override fun putLong(key: String?, value: Long): android.content.SharedPreferences.Editor = this
    override fun putFloat(key: String?, value: Float): android.content.SharedPreferences.Editor = this
    override fun putBoolean(key: String?, value: Boolean): android.content.SharedPreferences.Editor = this
    override fun remove(key: String?): android.content.SharedPreferences.Editor = this
    override fun clear(): android.content.SharedPreferences.Editor = this
    override fun commit(): Boolean = true
    override fun apply() {}
}
