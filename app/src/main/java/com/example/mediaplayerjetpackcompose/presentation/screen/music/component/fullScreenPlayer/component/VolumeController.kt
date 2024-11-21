package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeController(
  modifier: Modifier = Modifier,
  maxDeviceVolume: Int,
  currentVolume: Int,
  onVolumeChange: (Float) -> Unit,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
  ) {
    Icon(
      modifier = Modifier.size(18.dp),
      painter = painterResource(R.drawable.icon_volume_min),
      tint = Color.White.copy(alpha = 0.6f),
      contentDescription = "",
    )
    Slider(
      value = currentVolume.toFloat(),
      modifier = Modifier
        .width(280.dp),
      onValueChange = { value ->
        onVolumeChange(value)
      },
      thumb = {},
      track = { sliderState ->
        SliderDefaults.Track(
          modifier = Modifier.height(5.dp),
          sliderState = sliderState,
          colors = SliderDefaults.colors(
            activeTrackColor = Color.White.copy(alpha = 0.8f),
            inactiveTrackColor = Color.White.copy(alpha = 0.2f),
          ),
          drawStopIndicator = null,
          thumbTrackGapSize = 0.dp,
          trackInsideCornerSize = 0.dp,
        )
      },
      valueRange = 0f..maxDeviceVolume.toFloat(),
    )
    Icon(
      modifier = Modifier.size(18.dp),
      painter = painterResource(R.drawable.icon_volume_max),
      tint = Color.White.copy(alpha = 0.6f),
      contentDescription = "",
    )
  }
}