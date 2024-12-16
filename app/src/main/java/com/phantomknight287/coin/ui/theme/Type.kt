package com.phantomknight287.coin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.phantomknight287.coin.R

val GeistFontFamily = FontFamily(
    Font(
        R.font.geist,
    )
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GeistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = GeistFontFamily,
    ),
    bodySmall = TextStyle(fontFamily = GeistFontFamily),
    bodyMedium = TextStyle(fontFamily = GeistFontFamily),
    labelLarge = TextStyle(fontFamily = GeistFontFamily),
    labelSmall = TextStyle(fontFamily = GeistFontFamily),
    titleSmall = TextStyle(fontFamily = GeistFontFamily),
    labelMedium = TextStyle(fontFamily = GeistFontFamily),
    titleMedium = TextStyle(fontFamily = GeistFontFamily),
    displayLarge = TextStyle(fontFamily = GeistFontFamily),
    displaySmall = TextStyle(fontFamily = GeistFontFamily),
    displayMedium = TextStyle(fontFamily = GeistFontFamily),
    headlineMedium = TextStyle(fontFamily = GeistFontFamily),
    headlineLarge = TextStyle(fontFamily = GeistFontFamily),
    headlineSmall = TextStyle(fontFamily = GeistFontFamily),
)