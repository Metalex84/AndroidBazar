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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.example.androidbazar.common.MainHeader
import com.example.androidbazar.common.ResultsHeader
import com.example.androidbazar.common.TextTitle
import com.example.androidbazar.data.Item
import com.example.androidbazar.data.ProductsRepository

private const val SORT_PRICE = 0
private const val SORT_RATING = 1
private const val SORT_NONE = 2

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultsScreen(
    keywords: String,
    navController: NavController,
) {
    val context = LocalContext.current
    val repository = remember { ProductsRepository.create(context.applicationContext) }

    /** Getting products from data source. Remember to retrieve once only!  */
    val productsList = remember { repository.getAll() }

    /** Keywords - will filter the list */
    var typoSearch by rememberSaveable { mutableStateOf(keywords) }

    /** Categories - will filter the list */
    var categorySelected by rememberSaveable { mutableStateOf<String?>(null) }

    /** Sort criteria: */
    var sortBy by rememberSaveable { mutableIntStateOf(SORT_NONE) }

    /** FILTERING the actual list */
    val filteredList by produceState(
        initialValue = productsList,
        key1 = typoSearch,
        key2 = categorySelected,
        key3 = sortBy
    ) {
        val list = when (sortBy) {
            SORT_PRICE -> productsList.sortedBy { it.price }
            SORT_RATING -> productsList.sortedByDescending { it.rating }
            else -> productsList
        }
        value = list
            .filter {
                keywordSearch(it, typoSearch)
            }
            .filter {
                if (categorySelected == null)
                    true
                else
                    it.category == categorySelected
            }
    }

    /** List of categories based on the filtered list, not the whole collection */
    val eachCategoryList = filteredList.map { it.category }
        .groupingBy { it }
        .eachCount()
        .toList()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            MainHeader(
                value = typoSearch,
                onValueChange = { typoSearch = it },
                onNavigationClick = { navController.navigate("welcome_screen") }
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ResultsHeader(
                    keywords = typoSearch,
                    size = filteredList.size,
                )
                SortMenu(onSortSelected = { sortBy = it } )
            }
            CategoriesGroupedBy(
                eachCategoryList = eachCategoryList,
                selectedCategory = categorySelected,
                onCategoryClicked = { categorySelected = it },
            )
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 52.dp)
            ) {
                items(filteredList.size) { index ->
                    ProductCard(
                        context = context,
                        product = filteredList[index],
                        onNavigationClick = {
                            navController.navigate(route = "details_screen/${filteredList[index].id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard (
    context: Context,
    product: Item,
    onNavigationClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, end = 12.dp),
        onClick = { onNavigationClick() }
    ) {
        Row {
            ItemPicture(
                context = context,
                thumbnail = product.thumbnail,
                size = 160.dp
            )
            Column (
                horizontalAlignment = Alignment.Start
            ) {
                TextTitle(
                    title = product.title,
                    size = 22.sp
                )
                TextDescription(
                    description = product.description,
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
                        price = product.price,
                        size = 24.sp
                    )
                    CustomRatingBar(product.rating, Color.Yellow)
                }
            }
        }
    }
}

@Composable
fun CategoriesGroupedBy(
    eachCategoryList: List<Pair<String, Int>>,
    selectedCategory: String?,
    onCategoryClicked: (String?) -> Unit,
) {

    LazyRow (
        modifier = Modifier.width(340.dp)
    ) {
        items(eachCategoryList.size) { index ->
            FilterChip(
                selected = eachCategoryList[index].first == selectedCategory,
                onClick = {
                    if (eachCategoryList[index].first == selectedCategory)
                        onCategoryClicked(null)
                    else
                        onCategoryClicked(eachCategoryList[index].first)
                          },
                label = {
                    Text(
                        text = eachCategoryList[index].let { "${it.first}:${it.second}" },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                },
                leadingIcon = { if (eachCategoryList[index].first == selectedCategory)
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
            Spacer(modifier  = Modifier.padding(start = 8.dp))
        }
    }
}

private fun keywordSearch(it: Item, keywords: String) =
    it.title.contains(keywords, ignoreCase = true) ||
            it.description.contains(keywords, ignoreCase = true) ||
            it.category.contains(keywords, ignoreCase = true) ||
            it.brand?.contains(keywords, ignoreCase = true) ?: false


@Composable
private fun SortMenu(
    onSortSelected: (Int) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        stringResource(R.string.menu_sort_by_price),
        stringResource(R.string.menu_sort_by_rating)
    )
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clickable { expanded = true }
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton (
                onClick = { expanded = true }
            ) {
                Icon (
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Sort Menu Icon",
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSortSelected(options.indexOf(option))
                        expanded = false
                    }
                )
            }
        }
    }
}

