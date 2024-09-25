package com.example.androidbazar.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidbazar.R
import com.example.androidbazar.common.TopBar
import com.example.androidbazar.common.KeywordSearchBar
import com.example.androidbazar.common.MainActionButton
import com.example.androidbazar.common.AppMainPicture
import com.example.androidbazar.common.AppMainTitle

@Composable
fun WelcomeScreen (navController: NavController) {

    var typoSearch by rememberSaveable { mutableStateOf("") }

    Scaffold (
        topBar = {
            TopBar(
                text = stringResource(R.string.app_name),
                navigateBack = {},
                hasNavBack = false)
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
                AppMainPicture(
                    picture = R.drawable.shopping,
                    size = 240.dp
                )
                AppMainTitle(R.string.text_app_title)
                KeywordSearchBar(
                    value = typoSearch,
                    leadingIcon = Icons.Default.Search,
                    onValueChange = { typoSearch = it },
                    label = R.string.hint_keywords,
                )
                MainActionButton(
                    text = R.string.button_search,
                    onClick = { navController.navigate(route = "results_screen/${typoSearch}") }
                )
            }
        }

    }
}



