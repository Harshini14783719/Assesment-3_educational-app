package com.jcu.educationapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Landing : Screen("landing", "Home", Icons.Default.Home)
    data object Activity : Screen("activity", "Activity", Icons.Default.Psychology)
    data object UserStatistics : Screen("statistics", "Stats", Icons.Default.BarChart)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
