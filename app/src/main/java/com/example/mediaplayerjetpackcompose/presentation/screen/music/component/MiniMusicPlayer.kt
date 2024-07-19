package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.presentation.screen.component.MediaThumbnail
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MiniMusicPlayer(
  currentMediaCurrentState: MediaCurrentState,
  currentMusicPosition: () -> Long,
  modifier: Modifier,
  onClick: () -> Unit,
  onPauseMusic: () -> Unit,
  onNextMusic: () -> Unit,
  onPreviewMusic: () -> Unit,
  onResumeMusic: () -> Unit,
) {

  val duration = currentMediaCurrentState.metaData.extras?.getInt("Duration") ?: 0
  val mySize = remember { mutableFloatStateOf(0f) }
  val musicProgress = animateFloatAsState(
    targetValue = mySize.floatValue * (currentMusicPosition()
      .toFloat()
      .div(duration.toFloat())), label = ""
  )

  Card(
    onClick = { onClick.invoke() },
    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
    interactionSource = NoRippleEffect,
    modifier = modifier
      .fillMaxWidth(),
  ) {

    Column(
      verticalArrangement = Arrangement.Top,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 8.dp, top = 6.dp, bottom = 6.dp, end = 8.dp)
      ) {
        MediaThumbnail(
          uri = currentMediaCurrentState.metaData.artworkUri,
          size = 50.dp,
        )
        Column(
          modifier = Modifier
            .weight(2f)
            .padding(horizontal = 5.dp),
          verticalArrangement = Arrangement.Center,
        ) {
          Text(
            text = currentMediaCurrentState.metaData.title?.toString()?.removeFileExtension()
              ?: "Nothing Play",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
              .fillMaxWidth()
              .basicMarquee(),
            maxLines = 1,
          )
          Text(
            text = currentMediaCurrentState.metaData.artist?.toString() ?: "None",
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
              .fillMaxWidth(),
            maxLines = 1,
          )
        }
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
        ) {
          Icon(
            painter = painterResource(id = R.drawable.icon_skip_previous_24),
            contentDescription = "",
            modifier = Modifier
              .size(25.dp)
              .clickable {
                onPreviewMusic.invoke()
              },
            tint = MaterialTheme.colorScheme.onPrimary,
          )
          AnimatedContent(
            targetState = if (currentMediaCurrentState.isPlaying) R.drawable.icon_pause_24 else R.drawable.icon_play_arrow_24,
            label = "",
            modifier = Modifier.padding(10.dp)
          ) { int ->
            Icon(
              painter = painterResource(id = int),
              contentDescription = "",
              modifier = Modifier
                .size(35.dp)
                .clickable {
                  when (currentMediaCurrentState.isPlaying) {
                    true -> onPauseMusic.invoke()
                    false -> onResumeMusic.invoke()
                  }
                },
              tint = MaterialTheme.colorScheme.onPrimary,
            )
          }
          Icon(
            painter = painterResource(id = R.drawable.icon_skip_next_24),
            contentDescription = "",
            modifier = Modifier
              .size(25.dp)
              .clickable {
                onNextMusic.invoke()
              },
            tint = MaterialTheme.colorScheme.onPrimary,
          )
        }
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Text(
          text = currentMusicPosition().toInt().convertMilliSecondToTime(),
          fontSize = 14.sp,
          fontWeight = FontWeight.Normal,
        )
        Text(
          text = currentMediaCurrentState.metaData.extras?.getInt("Duration")?.convertMilliSecondToTime() ?: "None",
          fontSize = 14.sp,
          fontWeight = FontWeight.Normal,
        )
      }
      val color = MaterialTheme.colorScheme.onPrimary
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
          .drawBehind {
            mySize.floatValue = this.size.width
            clipRect(
              right = musicProgress.value,
            ) {
              this.drawRect(color = color)
            }
          },
      ) {}
    }
  }

}