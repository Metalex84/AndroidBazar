package com.example.androidbazar.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidbazar.R
import kotlin.math.ceil
import kotlin.math.floor


@Composable
fun KeywordSearchBar (
    @StringRes label: Int,
    leadingIcon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        leadingIcon = { Icon (imageVector = leadingIcon, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        onValueChange = onValueChange,
        label = { Text(stringResource(label))},
        modifier = modifier
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
fun ItemPicture(context: Context, thumbnail: String, size: Dp) {
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
fun TextTitle(title: String, size: TextUnit) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = size,
        modifier = Modifier.padding(top = 12.dp)
    )
}

@Composable
fun TextPrice(price: Double, size: TextUnit) {
    Text(
        text = "$price$",
        fontSize = size,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun TextDescription(
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

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: Int,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(top = 8.dp)
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun SecondaryButton(onClick: () -> Unit, text: Int) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun SearchBarPicture(picture: Int, size: Dp) {
    Image(
        painter = painterResource(picture),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .padding(top = 8.dp)
            .aspectRatio(1.0f)
            .clip(CircleShape)
    )
}


@Composable
fun AppMainTitle(
    picture: Int
) {
    Text(
        text = stringResource(picture),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}


@Composable
fun ResultsHeader(keywords: String?, size: Int) {
    Text(
        text = stringResource(R.string.text_search_results_param, keywords as String, size),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun MainHeader(
    navController: NavController,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
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
            value = value,
            leadingIcon = Icons.Default.Search,
            onValueChange = onValueChange,
            label = R.string.hint_keywords
        )
    }
}