package com.jcu.educationapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jcu.educationapp.ui.theme.IndigoPrimary
import com.jcu.educationapp.ui.theme.RubyError
import com.jcu.educationapp.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "App Settings & Preferences",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sound & Audio Settings Section
        Text(
            text = "Audio & Sensory Feedback",
            style = MaterialTheme.typography.titleLarge,
            color = IndigoPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsToggleCard(
            title = "Sound Effects",
            subtitle = "Play audio tones for correct and incorrect answers",
            icon = Icons.Default.GraphicEq,
            isChecked = uiState.soundEnabled,
            onCheckedChange = { viewModel.toggleSound(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsToggleCard(
            title = "Haptic Vibration",
            subtitle = "Vibrate device upon submitting answers",
            icon = Icons.Default.Vibration,
            isChecked = uiState.vibrationEnabled,
            onCheckedChange = { viewModel.toggleVibration(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Difficulty Level Section
        Text(
            text = "Quiz Difficulty Level",
            style = MaterialTheme.typography.titleLarge,
            color = IndigoPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = IndigoPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Select Question Difficulty", style = MaterialTheme.typography.titleLarge)
                }

                Spacer(modifier = Modifier.height(12.dp))

                listOf("Easy", "Medium", "Hard").forEach { difficulty ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.difficultyLevel == difficulty,
                            onClick = { viewModel.setDifficulty(difficulty) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = difficulty, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Data & Reset Section
        Text(
            text = "Data Management",
            style = MaterialTheme.typography.titleLarge,
            color = RubyError,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reset Quiz History", style = MaterialTheme.typography.titleLarge, color = RubyError)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Clear all stored Room database records", style = MaterialTheme.typography.bodyMedium)
                }

                Button(
                    onClick = { viewModel.showResetDialog(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = RubyError),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset")
                }
            }
        }
    }

    if (uiState.isResetDialogOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.showResetDialog(false) },
            title = { Text("Confirm Data Reset") },
            text = { Text("Are you sure you want to clear all your quiz history and score records from the database? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetData() },
                    colors = ButtonDefaults.buttonColors(containerColor = RubyError)
                ) {
                    Text("Clear Database")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showResetDialog(false) }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsToggleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = IndigoPrimary)
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = IndigoPrimary)
            )
        }
    }
}
