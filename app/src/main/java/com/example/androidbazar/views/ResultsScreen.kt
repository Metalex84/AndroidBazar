package com.example.androidbazar.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidbazar.R
import com.example.androidbazar.common.Price
import com.example.androidbazar.common.CustomRatingBar
import com.example.androidbazar.common.Description
import com.example.androidbazar.common.Thumbnail
import com.example.androidbazar.common.Title
import com.example.androidbazar.common.TopBar
import com.example.androidbazar.data.Item
import com.example.androidbazar.data.ProductsRepository

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultsScreen(
    keywords: String,
    navController: NavController,
    navigateBack: () -> Unit
) {

    val context = LocalContext.current
    val repository = remember { ProductsRepository.create(context.applicationContext) }

    // OJO, en algun momento "brand" es null
    val completeProductsList = remember { repository.getAll() }

    val productsList = completeProductsList.filter { keywordSearch(it, keywords) }

    Scaffold (
        topBar = {
            TopBar(
                text = stringResource(R.string.text_home),
                navigateBack = navigateBack,
                hasNavBack = true
            ) }
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
                SearchHeader(keywords, productsList.size)

                /* LISTA DE RESULTADOS */
                ListOfResults(productsList, context, navController)

            }
        }
    }
}

@Composable
private fun keywordSearch(it: Item, keywords: String) =
    it.title.contains(keywords, ignoreCase = true) ||
            it.description.contains(keywords, ignoreCase = true) ||
            it.category.contains(keywords, ignoreCase = true) ||
            it.brand?.contains(keywords, ignoreCase = true) ?: false


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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, end = 12.dp),
                onClick = {
                    navController.navigate(route = "details_screen/${productsList[index].id}")
                }
            ) {
                Row {
                    Thumbnail(
                        context = context,
                        thumbnail = productsList[index].thumbnail,
                        size = 160.dp
                    )
                    Column (
                        horizontalAlignment = Alignment.Start
                    ) {
                        Title(
                            title = productsList[index].title,
                            size = 22.sp
                        )
                        Description(
                            description = productsList[index].description,
                            size = 12.sp,
                            maxLines = 4,
                            paddingStart = 0.dp,
                            paddingEnd = 16.dp
                        )
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Price(
                                price = productsList[index].price,
                                size = 24.sp
                            )
                            CustomRatingBar(productsList[index].rating, Color.Yellow)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun SearchHeader(keywords: String?, size: Int) {
    Text(
        text = "Resultados de busqueda de '$keywords': $size",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold
    )
}