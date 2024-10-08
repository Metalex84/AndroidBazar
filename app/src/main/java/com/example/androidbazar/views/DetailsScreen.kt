package com.example.androidbazar.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidbazar.R
import com.example.androidbazar.common.CustomRatingBar
import com.example.androidbazar.common.CustomText
import com.example.androidbazar.common.TextDescription
import com.example.androidbazar.common.ItemPicture
import com.example.androidbazar.common.MainHeader
import com.example.androidbazar.data.ProductsRepository
import com.example.androidbazar.common.PrimaryButton
import com.example.androidbazar.common.SecondaryButton
import com.example.androidbazar.data.Item
import com.example.androidbazar.ui.theme.Price
import com.example.androidbazar.ui.theme.Subtitle
import com.example.androidbazar.ui.theme.NormalTitle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    productId: String,
    navController: NavController,
) {

    val context = LocalContext.current
    val repository = remember { ProductsRepository.create(context.applicationContext) }

    val detailedItem = repository.getItem(productId.toInt())

    val relatedItems = repository.getItemsByCategory(detailedItem.category)
        .filter { it.id != detailedItem.id }

    var typoSearch by rememberSaveable { mutableStateOf("") }

    val picturesList = mutableListOf<String>()
    picturesList.add(detailedItem.thumbnail)
    for (picture in detailedItem.images) {
        picturesList.add(picture)
    }

    var selectedImageIndex by remember { mutableIntStateOf(0) }

    Box (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainHeader(
                value = typoSearch,
                onValueChange = { typoSearch = it },
                onNavigationClick = { navController.navigate("welcome_screen") }
            )
            Row {
                SecondaryButton(
                    onClick = { navController.popBackStack() },
                    text = R.string.button_back
                )
                Spacer(modifier = Modifier.padding(start = 24.dp))
                PrimaryButton(
                    onClick = { navController.navigate(route = "results_screen/${typoSearch}") },
                    text = R.string.button_search
                )
            }
            Row {
                PictureCarousel(
                    picturesList = picturesList,
                    selectedImageIndex = selectedImageIndex,
                    detailedItem = detailedItem,
                    onSelectedImageIndexChange = { selectedImageIndex = it }
                )
            }
            CustomText(
                text = detailedItem.title + " - " + detailedItem.brand,
                style = NormalTitle
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                Column {
                    CustomText(
                        text = detailedItem.price.toString(),
                        style = Price
                        )
                    ItemAvailability(detailedItem)
                }
                Spacer(modifier = Modifier.padding(start = 24.dp))
                CustomRatingBar(
                    detailedItem.rating,
                    tint = Color.Yellow
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            TextDescription(
                description = detailedItem.description,
                size = 12.sp,
                maxLines = 3,
                paddingStart = 16.dp,
                paddingEnd = 16.dp
            )
            CustomText(
                text = stringResource(R.string.text_likely_items),
                style = Subtitle
            )
            RelatedItems(
                navController = navController,
                relatedItems = relatedItems
            )
            PrimaryButton(
                onClick = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_confirmation),
                        Toast.LENGTH_LONG
                    ).show()
                },
                text = R.string.text_buy,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
private fun PictureCarousel(
    picturesList: List<String>,
    selectedImageIndex: Int,
    detailedItem: Item,
    onSelectedImageIndexChange: (Int) -> Unit
) {
    ItemPicture(
        thumbnail = picturesList[selectedImageIndex],
        size = 200.dp
    )
    LazyColumn(
        modifier = Modifier.height(200.dp)
    ) {
        items(detailedItem.images.size) { index ->
            Box(
                modifier = Modifier.clickable { onSelectedImageIndexChange(index) }
            ) {
                ItemPicture(
                    thumbnail = picturesList[index],
                    size = 65.dp
                )
            }
        }
    }
}

@Composable
private fun ItemAvailability(
    detailedItem: Item
) {
    Text(
        text = buildString {
            append(detailedItem.stock.toString())
            append(stringResource(R.string.buildtext_availability))
        },
        fontSize = 10.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun RelatedItems(
    relatedItems: List<Item>,
    navController: NavController,
) {
    LazyRow (
        modifier = Modifier.width(340.dp)
    ) {
        items(relatedItems) { value ->
            Box(
                modifier = Modifier.clickable {
                    navController.navigate(route = "details_screen/${value.id}") }
            ) {
                ItemPicture (
                    thumbnail = value.thumbnail,
                    size = 78.dp
                )
            }
        }
    }
}