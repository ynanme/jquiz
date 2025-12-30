package com.example.jquiz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jquiz.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

fun typography (font: FontFamily): Typography {
    return Typography(
        bodyLarge = TextStyle(fontFamily = font),
        titleMedium = TextStyle(fontFamily = font),
        headlineMedium = TextStyle(fontFamily = font)
    )
}

val Tagesschrift = FontFamily(
    Font(R.font.tagesschrift, FontWeight.Normal),
    Font(R.font.tagesschrift, FontWeight.Bold)
)

val Saira = FontFamily(
    Font(R.font.saira, FontWeight.Normal),
    Font(R.font.saira, FontWeight.Bold)
)
val Christmas = FontFamily(
    Font(R.font.christmas, FontWeight.Normal),
    Font(R.font.christmas, FontWeight.Bold)
)