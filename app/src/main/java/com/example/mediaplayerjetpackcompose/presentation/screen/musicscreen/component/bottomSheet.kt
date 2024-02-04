package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomSheet(
  currentMusicState: MusicState,
  currentMusicPosition: Long,
  artworkImage: ImageBitmap,
  onDismissed: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
  onMoveNextMusic: () -> Unit,
  onMovePreviousMusic: () -> Unit,
  onSeekTo: (Long) -> Unit,
) {

  val imageSize = remember {
    mutableStateOf(IntSize.Zero)
  }

  Surface(modifier = Modifier.fillMaxSize()) {
    Image(
      bitmap = artworkImage,
      contentDescription = "",
      Modifier
        .fillMaxSize()
        .background(Color.Black)
        .onGloballyPositioned { imageSize.value = it.size }
        .drawWithCache {
          val color =
            Brush.verticalGradient(
              colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Black),
              startY = imageSize.value.height.toFloat() / 7,
              endY = imageSize.value.height.toFloat(),
            )
          onDrawWithContent {
            drawContent()
            drawRect(color)
          }
        },
      alignment = Alignment.Center,
      contentScale = ContentScale.Crop,
    )

    Column(
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxSize(),
    ) {

      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "",
        modifier = Modifier
          .size(50.dp)
          .clickable { onDismissed.invoke() },
        tint = Color.White,
      )

      Text(
        text = currentMusicState.metadata.artist?.toString() ?: "Nothing Play",
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(15.dp),
        color = Color.White,
      )

      Image(
        bitmap = artworkImage,
        contentDescription = "",
        modifier = Modifier
          .size(350.dp)
          .background(Color.Black, RoundedCornerShape(15.dp))
          .clip(RoundedCornerShape(15.dp))
      )

      Text(
        text = currentMusicState.metadata.title.toString(),
        fontSize = 20.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp)
          .basicMarquee(),
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        color = Color.White,
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
      ) {
        Text(
          text = currentMusicPosition.toInt().convertMilliSecondToTime(),
          fontSize = 13.sp,
          fontWeight = FontWeight.Normal,
          color = Color.White,
        )
        Text(
          text = currentMusicState.metadata.extras?.getInt("Duration")?.convertMilliSecondToTime()
            ?: "--:--",
          fontSize = 13.sp,
          fontWeight = FontWeight.Normal,
          color = Color.White,
        )
      }
      Slider(
        value = currentMusicPosition.toFloat(),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 15.dp),
        onValueChange = {
          onSeekTo.invoke(it.toLong())
        },
        valueRange = 0f..(currentMusicState.metadata.extras?.getInt("Duration")?.toFloat() ?: 0f),
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {

        Button(
          onClick = { onMovePreviousMusic.invoke() },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          Image(
            painter = painterResource(id = R.drawable.next),
            contentDescription = "",
            modifier = Modifier
              .size(25.dp)
              .rotate(180f),
            colorFilter = ColorFilter.tint(Color.White),
          )
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
          ) {
            Image(
              painter = painterResource(id = it),
              contentDescription = "",
              modifier = Modifier.size(55.dp),
              colorFilter = ColorFilter.tint(Color.White),
            )
          }
        }

        Button(
          onClick = {
            onMoveNextMusic.invoke()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          Image(
            painter = painterResource(id = R.drawable.next),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(Color.White),
          )
        }

      }
    }
  }
}
