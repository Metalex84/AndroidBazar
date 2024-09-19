package com.example.androidbazar.views

import android.app.appsearch.SearchResult.MatchInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidbazar.R
import com.example.androidbazar.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen (navController: NavController) {

    var typoSearch by rememberSaveable { mutableStateOf("") }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text( text = stringResource(R.string.app_name), fontWeight = FontWeight.Bold )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) {
        innerPadding ->
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onSecondaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            Column (
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 40.dp)
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image (
                    painter = painterResource(R.drawable.shopping),
                    contentDescription = null
                )
                Text(
                    text = "Bazar Online",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                TextField(
                    value = typoSearch,
                    //singleLine = true,
                    onValueChange = { typoSearch = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    label = { Text("laptops, smartphones...") },
                    leadingIcon = { Icon (imageVector = Icons.Default.Search, contentDescription = null) }
                )
                Button(
                    // TODO: cambiar esto y agregar parámetro de búsqueda (String que mete el usuario)
                    onClick = { navController.navigate(route = Screens.ResultsScreen.route) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Buscar")
                }
            }
        }

    }




}