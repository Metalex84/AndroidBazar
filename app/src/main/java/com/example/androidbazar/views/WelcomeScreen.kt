package com.example.androidbazar.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.androidbazar.R
import com.example.androidbazar.navigation.Screens

@Composable
fun WelcomeScreen (navController: NavController) {

    Column {
        Image (
            painter = painterResource(R.drawable.shopping),
            contentDescription = null
        )
        Text("Bazar Online")
        TextField(
            value = "",
            onValueChange = { },
            label = { Text("Search...") }
        )
        Button(
            // TODO: cambiar esto y agregar parámetro de búsqueda (String que mete el usuario)
            onClick = { navController.navigate(route = Screens.ResultsScreen.route)}
        ) {
            Text("Buscar")
        }
    }


}