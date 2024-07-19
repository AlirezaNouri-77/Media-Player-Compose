package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.data.util.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.convertToKbit
import com.example.mediaplayerjetpackcompose.data.util.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.presentation.screen.component.MediaThumbnail

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullMusicPlayer(
  favoriteList: SnapshotStateList<String>,
  currentMediaCurrentState: () -> MediaCurrentState,
  repeatMode: Int,
  currentMusicPosition: () -> Long,
  onDismissed: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
  onMoveNextMusic: () -> Unit,
  onMovePreviousMusic: () -> Unit,
  onRepeatMode: (Int) -> Unit,
  onSeekTo: (Long) -> Unit,
  onFavoriteToggle: () -> Unit,
) {

  val imageSize = remember {
    mutableStateOf(IntSize.Zero)
  }
  val seekPosition = remember {
    mutableFloatStateOf(0f)
  }
  var sliderInInteraction by remember {
    mutableStateOf(false)
  }
  val repeatModeIcon = remember(repeatMode) {
    when (repeatMode) {
      0 -> R.drawable.icon_repeat_off_24
      1 -> R.drawable.icon_repeat_one_24
      2 -> R.drawable.icon_repeat_all_24
      else -> -1
    }
  }
  val sliderValue = when (sliderInInteraction) {
    true -> seekPosition.floatValue
    false -> currentMusicPosition().toFloat()
  }

  val favIcon = when (currentMediaCurrentState().mediaId in favoriteList) {
    true -> Icons.Default.Favorite
    false -> Icons.Default.FavoriteBorder
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
    AsyncImage(
      model = currentMediaCurrentState().metaData.artworkUri,
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

      MediaThumbnail(uri = currentMediaCurrentState().metaData.artworkUri, size = 330.dp)

      Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 25.dp),
      ) {
        Text(
          text = currentMediaCurrentState().metaData.title?.toString()?.removeFileExtension()
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
          text = currentMediaCurrentState().metaData.artist?.toString() ?: "Nothing Play",
          fontSize = 21.sp,
          modifier = Modifier
            .basicMarquee(),
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          color = Color.White,
        )
        val songDetail = listOf(
          currentMediaCurrentState().metaData.title?.toString()?.extractFileExtension() ?: "None",
          currentMediaCurrentState().metaData.extras?.getInt("Bitrate")?.convertToKbit() ?: "None",
          currentMediaCurrentState().metaData.extras?.getInt("Size")?.convertByteToReadableSize()?.toString()
            ?: ""
        )
        Text(
          text = songDetail.reduce { acc, string -> "$acc, $string" },
          fontSize = 12.sp,
          color = Color.White,
        )
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Icon(
          imageVector = favIcon,
          contentDescription = "",
          modifier = Modifier
            .size(25.dp)
            .clickable {
              onFavoriteToggle.invoke()
            },
          tint = Color.White,
        )
        PlayerStateControllerButton(icon = repeatModeIcon, backgroundColor = Color.Transparent, size = 28.dp) {
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
            text = currentMusicPosition().toInt().convertMilliSecondToTime(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
          )
          Text(
            text = currentMediaCurrentState().metaData.extras?.getInt("Duration")?.convertMilliSecondToTime()
              .toString(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
          )
        }
        Slider(
          value = sliderValue,
          modifier = Modifier.fillMaxWidth(),
          onValueChange = { value ->
            seekPosition.floatValue = value
            sliderInInteraction = true
          },
          onValueChangeFinished = {
            sliderInInteraction = false
            onSeekTo.invoke(seekPosition.floatValue.toLong())
          },
          track = { sliderState ->
            SliderDefaults.Track(
              sliderState = sliderState,
              modifier = Modifier.scale(scaleX = 1f, scaleY = 2.2f),
              colors = SliderDefaults.colors(
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(0.5f),
              ),
            )
          },
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
          ),
          valueRange = 0f..(currentMediaCurrentState().metaData.extras?.getInt("Duration")?.toFloat() ?: 0f),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
        ) {
          PlayerStateControllerButton(
            icon = R.drawable.icon_skip_previous_24,
            radius = 1.2f,
            size = 30.dp
          ) {
            onMovePreviousMusic.invoke()
          }
          Spacer(modifier = Modifier.width(35.dp))
          PlayerStateControllerButton(
            icon = if (currentMediaCurrentState().isPlaying && !currentMediaCurrentState().isBuffering) R.drawable.icon_pause_24 else R.drawable.icon_play_arrow_24,
            radius = 1.5f,
            size = 55.dp
          ) {
            when (currentMediaCurrentState().isPlaying) {
              true -> onPauseMusic.invoke()
              false -> onResumeMusic.invoke()
            }
          }
          Spacer(modifier = Modifier.width(35.dp))
          PlayerStateControllerButton(icon = R.drawable.icon_skip_next_24, radius = 1.2f, size = 30.dp) {
            onMoveNextMusic.invoke()
          }
        }
      }
    }
  }
}