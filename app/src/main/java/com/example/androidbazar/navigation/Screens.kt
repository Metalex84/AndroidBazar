package com.example.androidbazar.navigation

sealed class Screens (val route: String) {
    data object WelcomeScreen: Screens("welcome_screen")
    data object ResultsScreen: Screens("results_screen")
    data object DetailsScreen: Screens("details_screen")
}