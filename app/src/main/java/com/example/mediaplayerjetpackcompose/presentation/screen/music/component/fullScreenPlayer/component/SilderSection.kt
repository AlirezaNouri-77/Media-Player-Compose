package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect

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
  var sliderInInteraction by remember {
    mutableStateOf(false)
  }
  val sliderTrackScale = remember {
    Animatable(0f)
  }
  val sliderValue = when (sliderInInteraction) {
    true -> seekPosition.floatValue
    false -> currentMusicPosition().toFloat()
  }

  val sliderTrackColorAlpha = if (sliderInInteraction) 0.2f else 0f
  LaunchedEffect(sliderInInteraction) {
    when (sliderInInteraction) {
      true -> sliderTrackScale.animateTo(1f)
      else -> sliderTrackScale.animateTo(0f)
    }
  }

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
      Slider(
        value = 0f,
        enabled = false,
        onValueChange = {},
        thumb = {
          SliderDefaults.Thumb(
            interactionSource = NoRippleEffect,
            thumbSize = DpSize.Zero,
          )
        },
        track = { sliderState ->
          SliderDefaults.Track(
            sliderState = sliderState,
            modifier = Modifier.graphicsLayer { scaleY = 2.3f },
            colors = SliderDefaults.colors(
              thumbColor = Color.Transparent,
              activeTrackColor = Color.Transparent,
              inactiveTrackColor = Color.White.copy(alpha = 0.2f),
            )
          )
        },
        valueRange = 0f..0f,
      )
      Slider(
        value = sliderValue,
        modifier = Modifier,
        onValueChange = { value ->
          seekPosition.floatValue = value
          sliderInInteraction = true
        },
        onValueChangeFinished = {
          sliderInInteraction = false
          seekTo.invoke(seekPosition.floatValue.toLong())
        },
        thumb = {
          SliderDefaults.Thumb(
            interactionSource = NoRippleEffect,
            thumbSize = DpSize.Zero
          )
        },
        track = { sliderState ->
          SliderDefaults.Track(
            sliderState = sliderState,
            modifier = Modifier.graphicsLayer {
              scaleY = 2.3f + sliderTrackScale.value
            },
            colors = SliderDefaults.colors(
              activeTrackColor = Color.White.copy(0.6f + sliderTrackColorAlpha),
              inactiveTrackColor = Color.Transparent,
            ),
          )
        },
        valueRange = 0f..duration,
      )
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
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
        text = duration.toInt().convertMilliSecondToTime(),
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = Color.White,
      )
    }
  }
}