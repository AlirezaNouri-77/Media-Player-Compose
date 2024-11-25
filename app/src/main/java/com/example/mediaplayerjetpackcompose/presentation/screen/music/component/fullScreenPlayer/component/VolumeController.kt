package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeController(
  modifier: Modifier = Modifier,
  maxDeviceVolume: Int,
  currentVolume: Int,
  onVolumeChange: (Float) -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  var isInteract by remember { mutableStateOf(false) }

  val sliderThumbWidth = animateDpAsState(targetValue = if (isInteract) 7.dp else 5.dp, label = "").value
  val sliderTrackHeight = animateDpAsState(targetValue = if (isInteract) 4.dp else 8.dp, label = "").value

  LaunchedEffect(interactionSource) {
    interactionSource.interactions.collect { interaction ->
      when (interaction) {
        is DragInteraction.Start -> isInteract = true
        is DragInteraction.Stop, is DragInteraction.Cancel -> isInteract = false
      }
    }
  }

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
  ) {
    Icon(
      modifier = Modifier.size(19.dp),
      painter = painterResource(R.drawable.icon_volume_min),
      tint = Color.White.copy(alpha = 0.6f),
      contentDescription = "",
    )
    Slider(
      value = currentVolume.toFloat() / maxDeviceVolume,
      modifier = Modifier
        .width(260.dp),
      onValueChange = { value ->
        onVolumeChange(value * maxDeviceVolume)
      },
      interactionSource = interactionSource,
      thumb = {
        SliderDefaults.Thumb(
          interactionSource = interactionSource,
          thumbSize = DpSize(width = sliderThumbWidth, height = 18.dp),
          colors = SliderDefaults.colors(
            thumbColor = Color.White.copy(alpha = 0.9f)
          ),
        )
      },
      track = { sliderState ->
        SliderDefaults.Track(
          modifier = Modifier
            .height(sliderTrackHeight)
            .clip(RoundedCornerShape(5.dp)),
          sliderState = sliderState,
          colors = SliderDefaults.colors(
            activeTrackColor = Color.White.copy(alpha = 0.8f),
            inactiveTrackColor = Color.White.copy(alpha = 0.2f),
          ),
          drawStopIndicator = null,
          thumbTrackGapSize = 3.dp,
          trackInsideCornerSize = 3.dp,
        )
      },
      valueRange = 0f..1f,
    )
    Icon(
      modifier = Modifier.size(19.dp),
      painter = painterResource(R.drawable.icon_volume_max),
      tint = Color.White.copy(alpha = 0.6f),
      contentDescription = "",
    )
  }
}

@Preview
@Composable
private fun Preview() {
  MediaPlayerJetpackComposeTheme {
    VolumeController(
      modifier = Modifier,
      maxDeviceVolume = 15,
      currentVolume = 4,
      onVolumeChange = {},
    )
  }
}