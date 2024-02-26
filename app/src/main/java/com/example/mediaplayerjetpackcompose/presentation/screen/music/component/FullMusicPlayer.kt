package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import android.graphics.Bitmap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.Constant
import com.example.mediaplayerjetpackcompose.data.MediaCurrentState
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.convertToKbit
import com.example.mediaplayerjetpackcompose.data.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.removeFileExtension
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullMusicPlayer(
  currentMediaCurrentState: MediaCurrentState,
  currentMusicPosition: State<Long>,
  repeatMode: Int,
  artworkImage: Bitmap,
  onDismissed: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
  onMoveNextMusic: () -> Unit,
  onMovePreviousMusic: () -> Unit,
  onRepeatMode: (Int) -> Unit,
  onSeekTo: (Long) -> Unit,
) {

  val imageSize = remember {
    mutableStateOf(IntSize.Zero)
  }
  val seekPosition = remember {
    mutableFloatStateOf(0f)
  }
  val sliderInInteraction = remember {
    mutableStateOf(false)
  }
  val repeatModeIcon = when (repeatMode) {
    0 -> R.drawable.icon_repeat_off_24
    1 -> R.drawable.icon_repeat_one_24
    2 -> R.drawable.icon_repeat_all_24
    else -> -1
  }

  Surface(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        this.detectDragGestures { change, dragAmount ->
          change.changedToUpIgnoreConsumed()
          if (dragAmount.y > 80f) {
            onDismissed.invoke()
          }
        }
      },
  ) {
    Image(
      bitmap = artworkImage.asImageBitmap(),
      contentDescription = "",
      Modifier
        .fillMaxSize()
        .blur(15.dp)
        .background(Color.Black)
        .onGloballyPositioned { imageSize.value = it.size }
        .drawWithCache {
          val color =
            Brush.verticalGradient(
              colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Black),
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
      modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 40.dp),
    ) {

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 15.dp),
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = "",
          modifier = Modifier
            .size(35.dp)
            .clickable { onDismissed.invoke() },
          tint = Color.White,
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(
          text = "Now Playing",
          modifier = Modifier.fillMaxWidth(),
          fontSize = 16.sp,
          fontWeight = FontWeight.Medium,
          color = Color.White,
        )
      }

      Box(
        modifier = Modifier
          .size(330.dp),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          bitmap = artworkImage.asImageBitmap(),
          contentDescription = "",
          modifier = Modifier
            .fillMaxSize()
            .background(Color.Black, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp)),
        )

        androidx.compose.animation.AnimatedVisibility(
          visible = sliderInInteraction.value,
          enter = fadeIn(tween(150)),
          exit = fadeOut(tween(150))
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Color.Black.copy(alpha = 0.5f),
                RoundedCornerShape(15.dp)
              ),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = seekPosition.floatValue.toInt().convertMilliSecondToTime(),
              color = Color.White,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier
            )
          }
        }

      }

      Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 25.dp),
      ) {
        Text(
          text = currentMediaCurrentState.metaData.title?.toString()?.removeFileExtension()
            ?: "Nothing Play",
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          maxLines = 1,
          modifier = Modifier
            .basicMarquee(),
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
          text = currentMediaCurrentState.metaData.artist?.toString() ?: "Nothing Play",
          fontSize = 21.sp,
          modifier = Modifier
            .basicMarquee(),
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          color = Color.White,
        )
        val songDetail = listOf(
          currentMediaCurrentState.metaData.title?.toString()?.extractFileExtension() ?: "None",
          currentMediaCurrentState.metaData.extras?.getInt("Bitrate")?.convertToKbit() ?: "None",
          currentMediaCurrentState.metaData.extras?.getInt("Size")?.convertByteToReadableSize()?.toString()
            ?: ""
        )
        Text(
          text = songDetail.reduce { acc, string -> "$acc, $string" },
          fontSize = 12.sp,
          color = Color.White,
        )
      }
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp), contentAlignment = Alignment.CenterEnd
      ) {
        ControlBottom(icon = repeatModeIcon, backgroundColor = Color.Transparent, size = 28.dp) {
          var mRepeatMode = repeatMode
          if (mRepeatMode++ >= Constant.RepeatModes.size.minus(1)) {
            onRepeatMode.invoke(0)
          } else {
            onRepeatMode.invoke(mRepeatMode)
          }
        }
      }
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 20.dp, start = 20.dp, bottom = 10.dp)
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
            text = currentMediaCurrentState.metaData.extras?.getInt("Duration")?.convertMilliSecondToTime()
              .toString(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
          )
        }
        Slider(
          value = currentMusicPosition.value.toFloat(),
          modifier = Modifier.fillMaxWidth(),
          onValueChange = { value ->
            sliderInInteraction.value = true
            seekPosition.floatValue = value
          },
          onValueChangeFinished = {
            sliderInInteraction.value = false
            onSeekTo.invoke(seekPosition.floatValue.toLong())
          },
          track = { sliderState ->
            SliderDefaults.Track(
              sliderState = sliderState,
              modifier = Modifier.scale(scaleX = 1f, scaleY = 2.5f),
              colors = SliderDefaults.colors(
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(0.5f),
              ),
            )
          },
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
          ),
          valueRange = 0f..(currentMediaCurrentState.metaData.extras?.getInt("Duration")?.toFloat() ?: 0f),

          )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
        ) {
          ControlBottom(
            icon = R.drawable.icon_skip_previous_24,
            radius = 1.2f,
            size = 30.dp
          ) {
            onMovePreviousMusic.invoke()
          }
          Spacer(modifier = Modifier.width(25.dp))
          ControlBottom(
            icon = if (currentMediaCurrentState.isPlaying && !currentMediaCurrentState.isBuffering) R.drawable.icon_pause_24 else R.drawable.icon_play_arrow_24,
            radius = 1.5f,
            size = 55.dp
          ) {
            when (currentMediaCurrentState.isPlaying) {
              true -> onPauseMusic.invoke()
              false -> onResumeMusic.invoke()
            }
          }
          Spacer(modifier = Modifier.width(25.dp))
          ControlBottom(icon = R.drawable.icon_skip_next_24, radius = 1.2f, size = 30.dp) {
            onMoveNextMusic.invoke()
          }
        }
      }
    }
  }
}

@Composable
fun ControlBottom(
  icon: Int,
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.White,
  backgroundAlpha: Float = 0.2f,
  radius: Float = 1f,
  size: Dp,
  onClick: () -> Unit,
) {
  Image(
    painter = painterResource(id = icon),
    contentDescription = "",
    modifier
      .size(size)
      .drawBehind {
        drawCircle(
          backgroundColor,
          center = this.center,
          radius = this.size.minDimension / radius,
          alpha = backgroundAlpha
        )
      }
      .clickable(interactionSource = NoRippleEffect, indication = null) {
        onClick.invoke()
      },
  )
}
