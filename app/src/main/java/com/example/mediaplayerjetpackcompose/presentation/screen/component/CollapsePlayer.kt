package com.example.mediaplayerjetpackcompose.presentation.screen.component

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.MediaCurrentState
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.removeFileExtension
import com.example.mediaplayerjetpackcompose.presentation.util.NoRippleEffect

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CollapsePlayer(
  currentMediaCurrentState: MediaCurrentState,
  currentMusicPosition: State<Long>,
  artworkImage: ImageBitmap,
  modifier: Modifier,
  onClick: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
) {

  val duration = currentMediaCurrentState.metaData.extras?.getInt("Duration") ?: 0
  val mySize = remember { mutableFloatStateOf(0f) }
  val musicProgress = animateFloatAsState(
    targetValue = mySize.floatValue * (currentMusicPosition.value
      .toFloat()
      .div(duration.toFloat())), label = ""
  )

  Card(
    onClick = { onClick.invoke() },
    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
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
        Image(
          bitmap = artworkImage,
          contentDescription = "",
          contentScale = ContentScale.Fit,
          alignment = Alignment.Center,
          modifier = Modifier
            .size(55.dp)
            .background(Color.Black, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp)),
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
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Text(
          text = currentMusicPosition.value.toInt().convertMilliSecondToTime(),
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