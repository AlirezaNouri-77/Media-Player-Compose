package com.example.mediaplayerjetpackcompose.data.util

import android.Manifest
import android.os.Build
import androidx.media3.common.Player

object Constant {

  val RepeatModes = listOf(
    Player.REPEAT_MODE_OFF,
    Player.REPEAT_MODE_ONE,
    Player.REPEAT_MODE_ALL
  )

  const val VIDEO_WIDTH = 150
  const val VIDEO_HEIGHT = 150
  val permissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(
      Manifest.permission.READ_MEDIA_VIDEO,
      Manifest.permission.READ_MEDIA_AUDIO,
    )
  } else {
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
  }

}