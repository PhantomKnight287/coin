package com.phantomknight287.coin.helpers

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager

fun getVibrator(context: Context): Vibrator {
    val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    return vib
}