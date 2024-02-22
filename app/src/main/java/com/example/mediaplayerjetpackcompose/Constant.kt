package com.example.mediaplayerjetpackcompose

import androidx.media3.common.Player

object Constant {
  val tabBarListItem = listOf("Music", "Artist", "Album")
  val RepeatModes = listOf(
    Player.REPEAT_MODE_OFF,
    Player.REPEAT_MODE_ONE,
    Player.REPEAT_MODE_ALL)
  const val FRAME_VIDEO = 5_000_000L
  const val VIDEO_WIDTH = 150
  const val VIDEO_HEIGHT = 150
}