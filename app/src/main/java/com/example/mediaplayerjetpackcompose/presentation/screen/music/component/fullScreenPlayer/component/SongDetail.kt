package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.data.util.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.util.convertToKbps
import com.example.mediaplayerjetpackcompose.data.util.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

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
        .fillMaxWidth()
        .basicMarquee(),
      text = currentCurrentMediaState().metaData.title?.removeFileExtension() ?: "Nothing Play",
      fontSize = 20.sp,
      fontWeight = FontWeight.SemiBold,
      overflow = TextOverflow.Visible,
      color = Color.White,
      maxLines = 1,
    )

    Text(
      modifier = Modifier
        .fillMaxWidth(),
      text = currentCurrentMediaState().metaData.artist?.toString() ?: "Nothing Play",
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium,
      maxLines = 1,
      color = Color.White.copy(alpha = 0.7f),
    )
    val songDetail = listOf(
      currentCurrentMediaState().metaData.title?.toString()?.extractFileExtension() ?: "None",
      currentCurrentMediaState().metaData.extras?.getInt(Constant.BITRATE_KEY)?.convertToKbps() ?: "None",
      currentCurrentMediaState().metaData.extras?.getInt(Constant.SIZE_KEY)?.convertByteToReadableSize()
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

@Preview()
@Composable
private fun FullScreenPreview() {
  MediaPlayerJetpackComposeTheme {
    SongDetail(
      currentCurrentMediaState = { CurrentMediaState.Empty }
    )
    }
  }