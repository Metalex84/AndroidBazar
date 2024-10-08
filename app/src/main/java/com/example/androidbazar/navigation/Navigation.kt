package com.example.androidbazar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
        composable(
            route = "results_screen/{keywords}",
            arguments = listOf(navArgument("keywords") { type = NavType.StringType } )
        ) { backStackEntry ->
            val keywords = backStackEntry.arguments?.getString("keywords")
            ResultsScreen(keywords.toString(), navController)
        }
        composable(
            route = "details_screen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType} )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            DetailsScreen(id.toString(), navController)
        }
    }
}