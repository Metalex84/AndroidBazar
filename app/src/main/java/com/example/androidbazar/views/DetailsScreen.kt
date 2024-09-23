package com.example.androidbazar.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.androidbazar.common.Price
import com.example.androidbazar.common.CustomRatingBar
import com.example.androidbazar.common.Description
import com.example.androidbazar.common.Thumbnail
import com.example.androidbazar.common.Title
import com.example.androidbazar.data.ProductsRepository
import com.example.androidbazar.navigation.Screens
import com.example.androidbazar.common.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    productId: String,
    navController: NavController,
    navigateBack: () -> Unit
) {

    val context = LocalContext.current
    val repository = remember { ProductsRepository.create(context.applicationContext) }

    val detailedItem = repository.getItem(productId.toInt())

    Scaffold (
        topBar = {
            TopBar(
                text = stringResource(R.string.title_product_detail),
                navigateBack = navigateBack,
                hasNavBack = true
            )
        }
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Thumbnail(
                    context = context,
                    thumbnail = detailedItem.thumbnail,
                    size = 240.dp
                )
                LazyColumn {
                    items(detailedItem.images.size) { product ->
                        Thumbnail(
                            context = context,
                            thumbnail = detailedItem.images[product],
                            size = 90.dp
                        )
                    }
                }
            }
            Title(
                title = detailedItem.title + " - " + detailedItem.brand,
                size = 32.sp
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                Column {
                    Price(
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
            Spacer(modifier = Modifier.padding(bottom = 24.dp))
            Description(
                description = detailedItem.description,
                size = 16.sp,
                maxLines = 3,
                paddingStart = 16.dp,
                paddingEnd = 16.dp
            )
            Row {
                Button(
                    onClick = { navController.navigate(route = Screens.WelcomeScreen.route) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = stringResource(R.string.text_home))
                }
                Spacer(modifier = Modifier.padding(start = 16.dp))
                PurchaseButton(context)
            }
        }
    }
}


@Composable
private fun PurchaseButton(context: Context) {
    Button(
        onClick = {
            Toast.makeText(
                context,
                context.getString(R.string.toast_confirmation),
                Toast.LENGTH_LONG
            ).show()
        },
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Text(text = stringResource(R.string.text_buy))
    }
}