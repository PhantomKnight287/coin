package com.phantomknight287.coin.constants

import androidx.compose.ui.graphics.Color

class CoinConstants {
    companion object {
        val GreySubtle = Color(0xFF9E9E9E)
        fun getBackgroundColor(darkTheme: Boolean): Color {
            return if (darkTheme) Color.Black else Color(0xffaeaeae)
        }

        fun getPrimaryTextColor(darkTheme: Boolean): Color {
            return if (darkTheme) Color.White else Color.Black
        }
    }
}