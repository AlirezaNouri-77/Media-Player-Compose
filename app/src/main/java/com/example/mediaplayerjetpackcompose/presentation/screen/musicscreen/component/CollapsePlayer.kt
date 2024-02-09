package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderPositions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.removeFileExtension

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
fun CollapsePlayer(
  currentMusicState: MusicState,
  currentMusicPosition: State<Long>,
  artworkImage: ImageBitmap,
  modifier: Modifier,
  onClick: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
) {

  val duration = remember(currentMusicState.isPlaying) {
    currentMusicState.metadata.extras?.getInt("Duration") ?: 0
  }

  Surface(
    modifier = modifier
      .clickable { onClick.invoke() }
      .drawWithCache {
        this.onDrawBehind {
          clipRect(
            right = size.width * (currentMusicPosition.value.toFloat().div(duration.toFloat()))
          ) {
            this.drawRect(color = Color.Black.copy(alpha = 0.2f))
          }
        }
      },
    color = Color.Transparent,
  ) {
    Column(
      verticalArrangement = Arrangement.Top,
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        Slider(
          value = currentMusicPosition.value.toFloat(),
          modifier = Modifier
            .height(8.dp)
            .fillMaxWidth(),
          onValueChange = {},
          thumb = {},
          track = {},
          valueRange = 0f..(currentMusicState.metadata.extras?.getInt("Duration")?.toFloat() ?: 0f),
          colors = SliderDefaults.colors(
            thumbColor = Color.Transparent,
            activeTrackColor = Color.Black,
            inactiveTrackColor = Color.Gray,
          ),
          enabled = false,
        )
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(6.dp),
      ) {
        Spacer(modifier = Modifier.width(3.dp))
        Image(
          bitmap = artworkImage,
          contentDescription = "",
          contentScale = ContentScale.Fit,
          alignment = Alignment.Center,
          modifier = Modifier
            .size(50.dp)
            .background(Color.Black, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp)),
        )
        Column(modifier = Modifier.weight(2f)) {
          Text(
            text = currentMusicState.metadata.title?.toString()?.removeFileExtension()
              ?: "Nothing Play",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 5.dp)
              .basicMarquee(),
            maxLines = 1,
          )
          Text(
            text = currentMusicState.metadata.artist?.toString() ?: "None",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 5.dp),
            maxLines = 1,
          )
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(IntrinsicSize.Min)
              .padding(horizontal = 5.dp),
          ) {
            Text(
              text = currentMusicPosition.value.toInt().convertMilliSecondToTime(),
              fontSize = 13.sp,
              fontWeight = FontWeight.Normal,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = currentMusicState.metadata.extras?.getInt("Duration")
                .convertMilliSecondToTime(),
              fontSize = 13.sp,
              fontWeight = FontWeight.Normal,
            )
          }
        }
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
              modifier = Modifier.size(30.dp),
            )
          }
        }
      }
    }
  }

}