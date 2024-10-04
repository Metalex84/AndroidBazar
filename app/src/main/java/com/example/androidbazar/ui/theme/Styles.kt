package com.example.androidbazar.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val TEXT_SIZE_HUGE = 32.sp
val TITLE_SIZE_BIG = 24.sp
val TITLE_SIZE_MEDIUM = 20.sp
val TEXT_SIZE_NORMAL = 18.sp


val MainTitle = TextStyle (
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.ExtraBold,
    fontSize = TEXT_SIZE_HUGE,
    )

val MiddleTitle = TextStyle (
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.ExtraBold,
    fontSize = TEXT_SIZE_NORMAL
)

val NormalTitle = TextStyle (
    fontWeight = FontWeight.ExtraBold,
    fontSize = TITLE_SIZE_BIG
)

val Subtitle = TextStyle (
    fontWeight = FontWeight.Bold,
    fontSize = TITLE_SIZE_MEDIUM
)

val Price = TextStyle (
    fontWeight = FontWeight.ExtraBold,
    fontSize = TITLE_SIZE_BIG
)