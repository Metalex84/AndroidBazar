package com.example.androidbazar.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidbazar.R
import com.example.androidbazar.common.TextPrice
import com.example.androidbazar.common.CustomRatingBar
import com.example.androidbazar.common.TextDescription
import com.example.androidbazar.common.ImageThumbnail
import com.example.androidbazar.common.TextTitle
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
    val productsList = remember { repository.getAll() }

    val searchedSubList = productsList.filter { keywordSearch(it, keywords) }

    val categoriesSublist = searchedSubList.map { it.category }
    val eachCategory = categoriesSublist.groupingBy { it }.eachCount()

    val buttonTypes = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surfaceContainer,
        MaterialTheme.colorScheme.surfaceContainerLow
    )

    Scaffold (
        topBar = {
            TopBar(
                text = stringResource(R.string.text_home),
                navigateBack = navigateBack,
                hasNavBack = true
            ) }
        ) {
            Column (
                modifier = Modifier.fillMaxSize()
            ) {
                /*
                * TODO: cabecera completa de la busqueda
                * */
                ResultsHeader(
                    keywords = keywords,
                    size = searchedSubList.size,
                )
                CategoriesOfSearch(
                    eachCategory = eachCategory,
                    buttonTypes = buttonTypes
                )
                ListOfResults(
                    productsList = searchedSubList,
                    context = context,
                    navController = navController
                )
            }
        }

}

@Composable
fun CategoriesOfSearch(eachCategory: Map<String, Int>, buttonTypes: List<Color>) {
    val categoryList = eachCategory.toList()
    var index = 0

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items(categoryList) { (key, value) ->
            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = buttonTypes[(index++%3)]
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier.padding(8.dp)
            ){
                Text(
                    text = "$key: $value",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier  = Modifier.padding(start = 8.dp))
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
    navController: NavController,
) {
    LazyColumn (
    ) {
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
                    ImageThumbnail(
                        context = context,
                        thumbnail = productsList[index].thumbnail,
                        size = 160.dp
                    )
                    Column (
                        horizontalAlignment = Alignment.Start
                    ) {
                        TextTitle(
                            title = productsList[index].title,
                            size = 22.sp
                        )
                        TextDescription(
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
                            TextPrice(
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

// TODO: es posible que haya que modificar el padding manual
@Composable
private fun ResultsHeader(keywords: String?, size: Int) {
    Text(
        text = "Resultados de busqueda de '$keywords': $size",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(top = 150.dp)
    )
}