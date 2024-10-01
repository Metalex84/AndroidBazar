package com.example.androidbazar.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.example.androidbazar.common.TextPrice
import com.example.androidbazar.common.CustomRatingBar
import com.example.androidbazar.common.TextDescription
import com.example.androidbazar.common.ItemPicture
import com.example.androidbazar.common.KeywordSearchBar
import com.example.androidbazar.common.TextTitle
import com.example.androidbazar.data.ProductsRepository
import com.example.androidbazar.common.PrimaryButton
import com.example.androidbazar.common.SearchBarPicture
import com.example.androidbazar.common.SecondaryButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    productId: String,
    navController: NavController,
) {

    val context = LocalContext.current
    val repository = remember { ProductsRepository.create(context.applicationContext) }

    // Recupero el producto como tal
    val detailedItem = repository.getItem(productId.toInt())

    // Recupero algunos items de la misma categoria excluyendo el elemento de muestra
    val relatedItems = repository.getItemsByCategory(detailedItem.category)
        .filter { it.id != detailedItem.id }

    // El texto de busqueda:
    var typoSearch by rememberSaveable { mutableStateOf("") }

    // Construyo la lista de imágenes
    val picturesList = mutableListOf<String>()
    picturesList.add(detailedItem.thumbnail)
    for (picture in detailedItem.images) {
        picturesList.add(picture)
    }

    // Recuerdo el índice (primero) de la colección
    var selectedImageIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Box(
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
            ItemPicture(
                context = context,
                thumbnail = picturesList[selectedImageIndex],
                size = 240.dp
            )
            LazyColumn(
                modifier = Modifier.height(222.dp)
            ) {
                items(detailedItem.images.size) { index ->
                    Box(
                        modifier = Modifier.clickable { selectedImageIndex = index }
                    ) {
                        ItemPicture(
                            context = context,
                            thumbnail = picturesList[index],
                            size = 72.dp
                        )
                    }
                }
            }
        }
        TextTitle(
            title = detailedItem.title + " - " + detailedItem.brand,
            size = 28.sp
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Row {
            Column {
                TextPrice(
                    price = detailedItem.price,
                    size = 24.sp
                )
                Text(
                    text = buildString {
                        append(detailedItem.stock.toString())
                        append(stringResource(R.string.buildtext_availability))
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
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
            size = 16.sp,
            maxLines = 3,
            paddingStart = 16.dp,
            paddingEnd = 16.dp
        )
        TextTitle(
            title = stringResource(R.string.text_likely_items),
            size = 18.sp
        )
        LazyRow (
            modifier = Modifier.width(340.dp)
        ) {
            items(relatedItems.size) { index ->
                Box(
                    modifier = Modifier.clickable { navController.navigate(
                        route = "details_screen/${relatedItems[index].id}"
                    ) }
                ) {
                    ItemPicture (
                        context = context,
                        thumbnail = relatedItems[index].thumbnail,
                        size = 82.dp
                    )
                }
            }
        }
        // TODO: este toast está fuera de la pantalla, no se ve
        PrimaryButton(
            onClick = {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_confirmation),
                    Toast.LENGTH_LONG
                ).show()
            },
            text = R.string.text_buy
        )
    }
}