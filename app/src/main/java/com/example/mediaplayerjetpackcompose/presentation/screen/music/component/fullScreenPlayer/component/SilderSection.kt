package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderSection(
  modifier: Modifier = Modifier,
  currentMusicPosition: () -> Long,
  seekTo: (Long) -> Unit,
  duration: Float,
) {

  val seekPosition = remember {
    mutableFloatStateOf(0f)
  }

  val interactionSource = remember { MutableInteractionSource() }
  var isInteraction by remember { mutableStateOf(false) }

  LaunchedEffect(interactionSource) {
    interactionSource.interactions.collect { interaction ->
      when (interaction) {
        is DragInteraction.Start -> isInteraction = true
        is DragInteraction.Cancel, is DragInteraction.Stop -> isInteraction = false
      }
    }
  }

  val sliderTrackHeight = animateDpAsState(targetValue = if (isInteraction) 12.dp else 8.dp, label = "").value

  val sliderValue = when (isInteraction) {
    true -> seekPosition.floatValue
    false -> currentMusicPosition().toFloat()
  }

  Column(
    modifier = modifier
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(2.dp)
  ) {
    Slider(
      value = sliderValue,
      interactionSource = interactionSource,
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 12.dp),
      onValueChange = { value ->
        seekPosition.floatValue = value
      },
      onValueChangeFinished = {
        seekTo.invoke(seekPosition.floatValue.toLong())
      },
      thumb = {},
      track = { sliderState ->
        SliderDefaults.Track(
          sliderState = sliderState,
          modifier = modifier.height(sliderTrackHeight),
          colors = SliderDefaults.colors(
            activeTrackColor = Color.White,
            inactiveTrackColor = Color.White.copy(alpha = 0.2f),
          ),
          drawStopIndicator = null,
          thumbTrackGapSize = 0.dp,
          trackInsideCornerSize = 0.dp,
        )
      },
      valueRange = 0f..duration,
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 2.dp),
      horizontalArrangement = Arrangement.Absolute.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = sliderValue.convertMilliSecondToTime(),
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = Color.White,
      )
      Text(
        text = duration.convertMilliSecondToTime(),
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = Color.White,
      )
    }

  }
}

@Preview
@Composable
private fun Preview() {
  MediaPlayerJetpackComposeTheme {
    SliderSection(
      modifier = Modifier,
      currentMusicPosition = { 50000 },
      seekTo = {},
      duration = 100000f,
    )
  }
}