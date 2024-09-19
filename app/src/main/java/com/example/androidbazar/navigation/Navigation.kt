package com.example.androidbazar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidbazar.views.DetailsScreen
import com.example.androidbazar.views.ResultsScreen
import com.example.androidbazar.views.WelcomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.WelcomeScreen.route
    ) {
        composable(route = Screens.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }
        // TODO: Aquí habrá que pasarle como argumentos la cadena de texto por la que el usuario realice la búsqueda
        composable(route = Screens.ResultsScreen.route) {
            ResultsScreen(navController) {
                navController.popBackStack()
            }
        }
        composable(route = Screens.DetailsScreen.route) {
            DetailsScreen(navController) {
                navController.popBackStack()
            }
        }
    }



}