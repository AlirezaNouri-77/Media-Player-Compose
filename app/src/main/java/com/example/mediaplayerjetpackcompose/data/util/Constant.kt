package com.example.mediaplayerjetpackcompose.data.util

import android.Manifest
import android.os.Build
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
  val permissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(
      Manifest.permission.READ_MEDIA_VIDEO,
      Manifest.permission.READ_MEDIA_AUDIO,
    )
  const val API_29_Q_ANDROID_10 =  Build.VERSION_CODES.Q
  } else {
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
  }

}