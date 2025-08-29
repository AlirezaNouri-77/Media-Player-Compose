package com.example.feature.video.util

import android.Manifest
import android.os.Build

object Constant {
    const val API_30_R_ANDROID_11 = Build.VERSION_CODES.R
    const val API_33_TIRAMISU_ANDROID_13 = Build.VERSION_CODES.TIRAMISU
    const val API_34_UPSIDE_DOWN_CAKE_ANDROID_14 = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
    const val API_29_Q_ANDROID_10 = Build.VERSION_CODES.Q

    val videoPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        arrayOf(Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_VIDEO)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
