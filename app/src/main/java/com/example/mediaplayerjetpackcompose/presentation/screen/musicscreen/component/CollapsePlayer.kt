package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.R

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CollapsePlayer(
  modifier: Modifier,
  currentMusicState: MusicState,
  onClick: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
    modifier = modifier
      .fillMaxWidth()
      .padding(5.dp)
      .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
      .clickable { onClick.invoke() },
  ) {
    Text(
      text = currentMusicState.metadata.title?.toString() ?: "Nothing Play",
      fontSize = 16.sp,
      fontWeight = FontWeight.Normal,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f, true)
        .padding(horizontal = 5.dp)
        .basicMarquee(),
      maxLines = 1,
    )
    Button(
      onClick = {
        when (currentMusicState.isPlaying) {
          true -> onPauseMusic.invoke()
          false -> onResumeMusic.invoke()
        }
      },
      colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    ) {
      AnimatedContent(
        targetState = if (currentMusicState.isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
        label = ""
      ) { int ->
        Image(
          painter = painterResource(id = int),
          contentDescription = "",
          modifier = Modifier.size(25.dp),
        )
      }
    }
  }
}