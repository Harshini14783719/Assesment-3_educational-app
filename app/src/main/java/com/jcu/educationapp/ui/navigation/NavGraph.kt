package com.jcu.educationapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jcu.educationapp.ui.screens.ActivityScreen
import com.jcu.educationapp.ui.screens.LandingScreen
import com.jcu.educationapp.ui.screens.SettingsScreen
import com.jcu.educationapp.ui.screens.UserStatisticsScreen
import com.jcu.educationapp.ui.theme.IndigoPrimary
import com.jcu.educationapp.viewmodel.LandingViewModel
import com.jcu.educationapp.viewmodel.QuizViewModel
import com.jcu.educationapp.viewmodel.SettingsViewModel
import com.jcu.educationapp.viewmodel.StatsViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    landingViewModel: LandingViewModel,
    quizViewModel: QuizViewModel,
    statsViewModel: StatsViewModel,
    settingsViewModel: SettingsViewModel
) {
    val items = listOf(
        Screen.Landing,
        Screen.Activity,
        Screen.UserStatistics,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndigoPrimary,
                            selectedTextColor = IndigoPrimary
                        ),
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Landing.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Landing.route) {
                LandingScreen(
                    viewModel = landingViewModel,
                    onStartQuizClicked = { category ->
                        quizViewModel.loadQuiz(category = category)
                        navController.navigate(Screen.Activity.route)
                    },
                    onViewStatsClicked = {
                        navController.navigate(Screen.UserStatistics.route)
                    }
                )
            }

            composable(Screen.Activity.route) {
                ActivityScreen(viewModel = quizViewModel)
            }

            composable(Screen.UserStatistics.route) {
                UserStatisticsScreen(viewModel = statsViewModel)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}
