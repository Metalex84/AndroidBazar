package com.example.androidbazar.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.androidbazar.common.ItemPicture
import com.example.androidbazar.common.KeywordSearchBar
import com.example.androidbazar.common.ResultsHeader
import com.example.androidbazar.common.SearchBarPicture
import com.example.androidbazar.common.TextTitle
import com.example.androidbazar.data.Item
import com.example.androidbazar.data.ProductsRepository

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultsScreen(
    keywords: String,
    navController: NavController,
) {
    val context = LocalContext.current
    val repository = remember { ProductsRepository.create(context.applicationContext) }

    val productsList = remember { repository.getAll() }

    var typoSearch by rememberSaveable { mutableStateOf(keywords) }

    val searchedSubList = productsList.filter { keywordSearch(it, typoSearch) }
    val categoriesSublist = searchedSubList.map { it.category }

    val eachCategory = categoriesSublist
        .groupingBy { it }
        .eachCount()
        .toList()

    // Manejo el cambio de estado por click en categoría
    var categorySelected by rememberSaveable { mutableStateOf("") }

    var filteredList: List<Item> = if (categorySelected == "") {
        searchedSubList
    } else {
        searchedSubList.filter { it.category == categorySelected }
    }


    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Box (
                modifier = Modifier.clickable {
                    navController.navigate(route = "welcome_screen")
                }
            ) {
                SearchBarPicture(
                    picture = R.drawable.shopping,
                    size = 84.dp
                )
            }
            KeywordSearchBar(
                value = typoSearch,
                leadingIcon = Icons.Default.Search,
                onValueChange = { typoSearch = it },
                label = R.string.hint_keywords
            )
        }
        ResultsHeader(
            keywords = typoSearch,
            size = filteredList.size,
        )
        CategoriesGroupedBy(
            eachCategory = eachCategory,
            onCategoryClicked = { categorySelected = it }
        )
        ListOfResults(
            productsList = filteredList,
            context = context,
            navController = navController
        )
    }
}

@Composable
fun CategoriesGroupedBy(
    eachCategory: List<Pair<String, Int>>,
    onCategoryClicked: (String) -> Unit
) {
    val buttonTypes = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surfaceContainer,
        MaterialTheme.colorScheme.surfaceContainerLow
    )
    var i = 0

    LazyRow (
        modifier = Modifier.width(360.dp)
    ) {
        items(eachCategory.size) { index ->
            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = buttonTypes[(i++%3)]
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onCategoryClicked(eachCategory[index].first) }
            ){
                Text(
                    text = eachCategory[index].let { "${it.first}:${it.second}" },
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
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 52.dp)
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
                    ItemPicture(
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

