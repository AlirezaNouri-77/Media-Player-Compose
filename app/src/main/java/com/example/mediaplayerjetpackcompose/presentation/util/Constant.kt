package com.example.mediaplayerjetpackcompose.presentation.util

import android.Manifest
import android.os.Build
import androidx.compose.ui.unit.dp

object Constant {
    const val API_30_R_ANDROID_11 = Build.VERSION_CODES.R
    const val API_33_TIRAMISU_ANDROID_13 = Build.VERSION_CODES.TIRAMISU
    const val API_34_UPSIDE_DOWN_CAKE_ANDROID_14 = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
    const val API_29_Q_ANDROID_10 = Build.VERSION_CODES.Q
    val navigationBarHeight = 80.dp

    val musicPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
