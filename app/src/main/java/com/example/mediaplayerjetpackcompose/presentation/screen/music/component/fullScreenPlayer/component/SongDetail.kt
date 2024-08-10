package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.data.util.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.util.convertToKbit
import com.example.mediaplayerjetpackcompose.data.util.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongDetail(
  modifier: Modifier = Modifier,
  currentMediaCurrentState: () -> MediaCurrentState,
) {
  AnimatedContent(
    targetState = currentMediaCurrentState().metaData,
    label = "",
  ) { target ->
    Column(
      horizontalAlignment = Alignment.Start,
      modifier = modifier,
    ) {
      Text(
        modifier = Modifier
          .basicMarquee(),
        text = target.title?.toString()?.removeFileExtension()
          ?: "Nothing Play",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        maxLines = 1,
      )
      Text(
        text = target.artist?.toString() ?: "Nothing Play",
        fontSize = 14.sp,
        modifier = Modifier,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        color = Color.White.copy(alpha = 0.7f),
      )
      val songDetail = listOf(
        target.title?.toString()?.extractFileExtension() ?: "None",
        target.extras?.getInt("Bitrate")?.convertToKbit() ?: "None",
        target.extras?.getInt("Size")?.convertByteToReadableSize()?.toString()
          ?: ""
      )
      Text(
        text = songDetail.reduce { acc, string -> "$acc, $string" },
        fontSize = 12.sp,
        color = Color.White.copy(alpha = 0.7f),
      )
    }
  }

}