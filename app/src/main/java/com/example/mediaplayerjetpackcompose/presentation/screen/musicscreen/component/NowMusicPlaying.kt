package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.convertToKbit
import com.example.mediaplayerjetpackcompose.data.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.removeFileExtension
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowMusicPlaying(
  currentMusicState: MusicState,
  currentMusicPosition: State<Long>,
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

  Surface(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        this.detectDragGestures { change, dragAmount ->
          change.changedToUpIgnoreConsumed()
          if (dragAmount.y > 0f) {
            onDismissed.invoke()
          }
        }
      },
  ) {
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

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(5.dp),
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = "",
          modifier = Modifier
            .size(45.dp)
            .clickable { onDismissed.invoke() },
          tint = Color.White,
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(
          text = "Now Playing",
          modifier = Modifier.fillMaxWidth(),
          fontSize = 18.sp,
          fontWeight = FontWeight.Medium,
          color = Color.White,
        )
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = currentMusicState.metadata.title?.toString()?.removeFileExtension()
            ?: "Nothing Play",
          fontSize = 25.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          maxLines = 1,
          modifier = Modifier
            .padding(horizontal = 10.dp)
            .basicMarquee(),
        )
        Spacer(modifier = Modifier.height(15.dp))
        Image(
          bitmap = artworkImage,
          contentDescription = "",
          modifier = Modifier
            .size(350.dp)
            .background(Color.Black, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
        )
      }

      Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 25.dp),
      ) {
        Text(
          text = currentMusicState.metadata.artist?.toString() ?: "Nothing Play",
          fontSize = 20.sp,
          modifier = Modifier
            .basicMarquee(),
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          color = Color.White,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row {
          Text(
            text = currentMusicState.metadata.title?.toString()?.extractFileExtension() ?: "None",
            fontSize = 14.sp,
            color = Color.White,
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = currentMusicState.metadata.extras?.getInt("Bitrate")?.convertToKbit().toString(),
            fontSize = 14.sp,
            color = Color.White,
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = currentMusicState.metadata.extras?.getInt("Size")
              ?.convertByteToReadableSize()?.toString() ?: "",
            fontSize = 14.sp,
            color = Color.White,
          )
        }

      }

      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 20.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        ) {
          Text(
            text = currentMusicPosition.value.toInt().convertMilliSecondToTime(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
          )
          Text(
            text = currentMusicState.metadata.extras?.getInt("Duration")?.convertMilliSecondToTime().toString(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
          )
        }
        Slider(
          value = currentMusicPosition.value.toFloat(),
          modifier = Modifier
            .fillMaxWidth(),
          onValueChange = {
            onSeekTo.invoke(it.toLong())
          },
          valueRange = 0f..(currentMusicState.metadata.extras?.getInt("Duration")?.toFloat() ?: 0f),
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.White,
            inactiveTrackColor = Color.White.copy(0.6f),
          ),
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
              painter = painterResource(id = R.drawable.icon_next),
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
              painter = painterResource(id = R.drawable.icon_next),
              contentDescription = "",
              modifier = Modifier.size(25.dp),
              colorFilter = ColorFilter.tint(Color.White),
            )
          }
        }
      }

    }
  }
}
