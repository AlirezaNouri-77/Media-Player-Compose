package com.example.mediaplayerjetpackcompose.util

import android.Manifest
import android.os.Build
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player

object Constant {

  val RepeatModes = mapOf(
    0 to Player.REPEAT_MODE_OFF,
    1 to Player.REPEAT_MODE_ONE,
    2 to Player.REPEAT_MODE_ALL
  )

  const val DURATION_KEY = "Duration"
  const val BITRATE_KEY = "Bitrate"
  const val SIZE_KEY = "Size"

  const val VIDEO_WIDTH = 150
  const val VIDEO_HEIGHT = 150

  val MINI_PLAYER_HEIGHT = 70.dp

  const val API_30_R_ANDROID_11 = Build.VERSION_CODES.R
  const val API_33_TIRAMISU_ANDROID_13 = Build.VERSION_CODES.TIRAMISU
  const val API_34_UPSIDE_DOWN_CAKE_ANDROID_14 = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
  const val API_29_Q_ANDROID_10 =  Build.VERSION_CODES.Q

  val musicPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_AUDIO
  } else {
    Manifest.permission.READ_EXTERNAL_STORAGE
  }

  val videoPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
    arrayOf(Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(Manifest.permission.READ_MEDIA_VIDEO)
  } else {
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
  }

}