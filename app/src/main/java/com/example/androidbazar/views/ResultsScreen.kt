package com.example.androidbazar.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidbazar.R
import com.example.androidbazar.data.Item
import com.example.androidbazar.data.ProductsRepository
import com.example.androidbazar.navigation.Screens
import kotlin.math.ceil
import kotlin.math.floor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController, navigateBack: () -> Unit) {

    val context = LocalContext.current
    val repository = remember { ProductsRepository.create(context.applicationContext) }

    val productsList = remember { repository.getAll() } // OJO, en algun momento "brand" es null

    Scaffold (
        topBar = { TopBar(navigateBack) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onSecondaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /*
                * TODO: cabecera completa de la busqueda
                * */
                SearchHeader()

                /* LISTA DE RESULTADOS */
                ListOfResults(productsList, context, navController)

            }
        }
    }
}

@Composable
private fun ListOfResults(
    productsList: List<Item>,
    context: Context,
    navController: NavController
) {
    LazyColumn {
        items(productsList.size) { index ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, end = 12.dp),
                onClick = { navController.navigate(route = Screens.DetailsScreen.route) }
            ) {
                Row {
                    AsyncImage(
                        model = ImageRequest.Builder(context = context)
                            .data(productsList[index].thumbnail)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Item thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .padding(16.dp)
                            .aspectRatio(1.0f)
                            .clip(CircleShape)
                    )
                    Column (
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = productsList[index].title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                        Text(
                            text = productsList[index].description,
                            fontSize = 12.sp
                        )
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = productsList[index].price.toString() + "$",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = productsList[index].rating.toString()
                            )
                        }
                    }

                }

            }
        }
    }
}


@Composable
private fun SearchHeader() {
    Text(
        text = "Resultados de busqueda de xxxx",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(navigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = Color.Black
        ),
        title = {
            Text(
                text = stringResource(R.string.text_home),
                fontStyle = FontStyle.Italic
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}
