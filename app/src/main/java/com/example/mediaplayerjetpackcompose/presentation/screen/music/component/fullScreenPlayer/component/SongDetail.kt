package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.util.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.util.convertToKbit
import com.example.mediaplayerjetpackcompose.data.util.extractFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState

@Composable
fun SongDetail(
  modifier: Modifier = Modifier,
  currentCurrentMediaState: () -> CurrentMediaState,
) {

  Column(
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    modifier = modifier
  ) {

    Text(
      modifier = Modifier
        .fillMaxWidth(),
      text = currentCurrentMediaState().metaData.artist?.toString() ?: "Empty Artist",
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium,
      maxLines = 1,
      color = Color.White.copy(alpha = 0.7f),
    )
    val songDetail = listOf(
      currentCurrentMediaState().metaData.title?.toString()?.extractFileExtension() ?: "None",
      currentCurrentMediaState().metaData.extras?.getInt("Bitrate")?.convertToKbit() ?: "None",
      currentCurrentMediaState().metaData.extras?.getInt("Size")?.convertByteToReadableSize()?.toString()
        ?: ""
    )
    Text(
      modifier = Modifier
        .fillMaxWidth(),
      text = songDetail.reduce { acc, string -> "$acc, $string" },
      fontSize = 12.sp,
      color = Color.White.copy(alpha = 0.7f),
    )
  }

}