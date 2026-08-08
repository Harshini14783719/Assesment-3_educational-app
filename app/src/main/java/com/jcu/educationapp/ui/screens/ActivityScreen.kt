package com.jcu.educationapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jcu.educationapp.ui.theme.EmeraldSuccess
import com.jcu.educationapp.ui.theme.IndigoPrimary
import com.jcu.educationapp.ui.theme.RubyError
import com.jcu.educationapp.viewmodel.QuizUiState
import com.jcu.educationapp.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    viewModel: QuizViewModel,
    initialCategory: String = "Science",
    onNavigateHome: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Flashcards First, 1 = Quiz Mode

    LaunchedEffect(initialCategory) {
        viewModel.loadQuiz(category = initialCategory)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Interactive Learning", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onNavigateHome) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Return Home")
                }
            },
            actions = {
                IconButton(onClick = onNavigateHome) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Tab Header: Flashcards First, then Quiz
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = IndigoPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("1. Active Flashcards", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("2. Take STEM Quiz", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            FlashcardTabContent(
                category = initialCategory,
                onStartQuiz = { selectedTab = 1 }
            )
        } else {
            QuizTabContent(viewModel = viewModel, onNavigateHome = onNavigateHome)
        }
    }
}

@Composable
private fun FlashcardTabContent(
    category: String,
    onStartQuiz: () -> Unit
) {
    var cardFlipped by remember { mutableStateOf(false) }
    var currentCardIndex by remember { mutableIntStateOf(0) }

    val flashcards = remember(category) {
        if (category == "Computers") {
            listOf(
                "Algorithm" to "A self-contained step-by-step set of operations to solve a problem or perform a computation.",
                "API (Application Programming Interface)" to "A set of protocols, routines, and tools for building software applications and connecting services.",
                "Encapsulation" to "In Object-Oriented Programming, bundling data with the methods operating on that data and restricting direct access.",
                "Binary System" to "A base-2 number system representing numeric values using two symbols: 0 and 1.",
                "Jetpack Compose" to "Android's modern declarative UI toolkit for building native user interfaces efficiently in Kotlin."
            )
        } else {
            listOf(
                "Photosynthesis" to "The biological process by which green plants use sunlight to synthesize nutrients from carbon dioxide and water.",
                "Mitochondria" to "The power-producing organelle inside eukaryotic cells that converts glucose into cellular ATP energy.",
                "DNA (Deoxyribonucleic Acid)" to "The molecule carrying genetic instructions for the development, functioning, and reproduction of living organisms.",
                "Newton's First Law" to "An object will remain at rest or in uniform motion in a straight line unless acted upon by an external force.",
                "Atomic Number" to "The number of protons found in the nucleus of an atom, uniquely identifying a chemical element."
            )
        }
    }

    val currentPair = flashcards[currentCardIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(IndigoPrimary.copy(alpha = 0.1f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Study First: $category Flashcards",
                color = IndigoPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Card ${currentCardIndex + 1} of ${flashcards.size}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Flip Flashcard Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clickable { cardFlipped = !cardFlipped },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (cardFlipped) IndigoPrimary else MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (cardFlipped) "DEFINITION" else "TERM (Tap to flip)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (cardFlipped) Color.White.copy(alpha = 0.8f) else IndigoPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (cardFlipped) currentPair.second else currentPair.first,
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (cardFlipped) Color.White else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "💡 Tap card to flip between Term and Definition",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card navigation row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (currentCardIndex > 0) {
                        currentCardIndex--
                        cardFlipped = false
                    }
                },
                enabled = currentCardIndex > 0
            ) {
                Text("Previous")
            }

            Button(
                onClick = {
                    if (currentCardIndex + 1 < flashcards.size) {
                        currentCardIndex++
                        cardFlipped = false
                    }
                },
                enabled = currentCardIndex + 1 < flashcards.size
            ) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Ready to Quiz CTA button
        Button(
            onClick = onStartQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Reviewed Cards? Start Quiz Now →",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuizTabContent(viewModel: QuizViewModel, onNavigateHome: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is QuizUiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = IndigoPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Fetching STEM content...", style = MaterialTheme.typography.bodyLarge)
                }
            }

            is QuizUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        color = RubyError,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadQuiz("Science") },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Retry")
                    }
                }
            }

            is QuizUiState.Active -> {
                ActiveQuizView(
                    state = state,
                    onOptionSelected = { viewModel.selectOption(it) },
                    onSubmitAnswer = { viewModel.submitAnswer() },
                    onNextQuestion = { viewModel.nextQuestion() }
                )
            }

            is QuizUiState.Completed -> {
                CompletedQuizView(
                    state = state,
                    onRestartClicked = { viewModel.loadQuiz(state.category) },
                    onNavigateHome = onNavigateHome
                )
            }
        }
    }
}

@Composable
private fun ActiveQuizView(
    state: QuizUiState.Active,
    onOptionSelected: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val currentQuestion = state.questions[state.currentIndex]
    val progress = (state.currentIndex + 1).toFloat() / state.questions.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Progress bar & Timer header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${state.currentIndex + 1} of ${state.questions.size}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = if (state.timerSecondsRemaining < 5) RubyError else IndigoPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${state.timerSecondsRemaining}s",
                    fontWeight = FontWeight.Bold,
                    color = if (state.timerSecondsRemaining < 5) RubyError else IndigoPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = IndigoPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(IndigoPrimary.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${state.category} • ${state.difficulty}",
                        color = IndigoPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Multiple Choice Options
        currentQuestion.options.forEach { option ->
            val isSelected = state.selectedOption == option
            val isCorrect = state.isAnswerSubmitted && option == currentQuestion.correctAnswer
            val isWrongSelection = state.isAnswerSubmitted && isSelected && option != currentQuestion.correctAnswer

            val backgroundColor = when {
                isCorrect -> EmeraldSuccess.copy(alpha = 0.2f)
                isWrongSelection -> RubyError.copy(alpha = 0.2f)
                isSelected -> IndigoPrimary.copy(alpha = 0.15f)
                else -> MaterialTheme.colorScheme.surface
            }

            val borderColor = when {
                isCorrect -> EmeraldSuccess
                isWrongSelection -> RubyError
                isSelected -> IndigoPrimary
                else -> Color.Transparent
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                    .clickable(enabled = !state.isAnswerSubmitted) {
                        onOptionSelected(option)
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (isCorrect) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess)
                    } else if (isWrongSelection) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = RubyError)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Answer Explanation box when submitted
        AnimatedVisibility(
            visible = state.isAnswerSubmitted,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = IndigoPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Explanation", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(currentQuestion.explanation, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Button (Submit or Next)
        if (!state.isAnswerSubmitted) {
            Button(
                onClick = onSubmitAnswer,
                enabled = state.selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Text("Submit Answer", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onNextQuestion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Text(
                    text = if (state.currentIndex + 1 < state.questions.size) "Next Question" else "View Results",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CompletedQuizView(
    state: QuizUiState.Completed,
    onRestartClicked: () -> Unit,
    onNavigateHome: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(EmeraldSuccess.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = EmeraldSuccess,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Quiz Completed!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Category: ${state.category}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "${state.finalScore} / ${state.totalQuestions}",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = IndigoPrimary
            )

            Text(
                text = "${state.percentage.toInt()}% Accuracy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldSuccess
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Total Time: ${state.timeTakenSeconds} seconds",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onRestartClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Take Another Quiz", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onNavigateHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Home, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Return to Home", fontSize = 16.sp)
            }
        }
    }
}
