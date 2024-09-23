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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidbazar.R
import com.example.androidbazar.common.TopBar
import com.example.androidbazar.data.Item
import com.example.androidbazar.data.ProductsRepository
import kotlin.math.ceil
import kotlin.math.floor

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
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, end = 12.dp),
                onClick = {
                    navController.navigate(route = "details_screen/${productsList[index].id}")
                }
            ) {
                Row {
                    Thumbnail(context, productsList, index)
                    Column (
                        horizontalAlignment = Alignment.Start
                    ) {
                        Title(productsList, index)
                        Description(productsList, index)
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Price(productsList, index)
                            RatingBar(productsList[index].rating, Color.Yellow)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Price(
    productsList: List<Item>,
    index: Int
) {
    Text(
        text = productsList[index].price.toString() + "$",
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun Description(
    productsList: List<Item>,
    index: Int
) {
    Text(
        text = productsList[index].description,
        fontSize = 12.sp
    )
}

@Composable
private fun Title(
    productsList: List<Item>,
    index: Int
) {
    Text(
        text = productsList[index].title,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = Modifier.padding(top = 20.dp)
    )
}

@Composable
private fun Thumbnail(
    context: Context,
    productsList: List<Item>,
    index: Int
) {
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
}

@Composable
fun RatingBar(
    rating: Double = 0.0,
    tint: Color
) {
    val stars = 5

    val filledStars = floor(rating).toInt()
    val unfilledStars = ceil(stars.toDouble() - filledStars - 1)
    val halfStar = rating % 1 != 0.0

    repeat(filledStars) {
        Icon(
            painter = painterResource(R.drawable.baseline_star_24),
            contentDescription = null,
            tint = tint
        )
    }
    if (halfStar) {
         Icon(
             painter = painterResource(R.drawable.baseline_star_half_24),
             contentDescription = null,
             tint = tint
         )
    }
    repeat(unfilledStars.toInt()) {
        Icon(
            painter = painterResource(R.drawable.outline_star_outline_24),
            contentDescription = null,
            tint = tint
        )
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