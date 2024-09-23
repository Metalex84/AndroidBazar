package com.example.androidbazar.common

import android.content.Context
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidbazar.R
import kotlin.math.ceil
import kotlin.math.floor


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    text: String,
    navigateBack: () -> Unit,
    hasNavBack: Boolean
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = Color.Black
        ),
        title = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (hasNavBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
    )
}


@Composable
fun CustomRatingBar(rating: Double = 0.0, tint: Color) {
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
fun Thumbnail(context: Context, thumbnail: String, size: Dp) {
    AsyncImage(
        model = ImageRequest.Builder(context = context)
            .data(thumbnail)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.item_thumbnail),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .padding(16.dp)
            .aspectRatio(1.0f)
            .clip(CircleShape)
    )
}

@Composable
fun Title(title: String, size: TextUnit) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = size,
        modifier = Modifier.padding(top = 20.dp)
    )
}

@Composable
fun Price(price: Double, size: TextUnit) {
    Text(
        text = "$price$",
        fontSize = size,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun Description(
    description: String,
    size: TextUnit,
    maxLines: Int,
    paddingStart: Dp,
    paddingEnd: Dp
) {
    Text(
        text = description,
        fontSize = size,
        maxLines = maxLines,
        softWrap = true,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(start = paddingStart, end = paddingEnd)
    )
}