package com.example.feature.music_player.fullScreen.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.util.convertMilliSecondToTime

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
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

  val sliderInteractionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
  var isSliderInteraction by remember { mutableStateOf(false) }

  LaunchedEffect(sliderInteractionSource) {
    sliderInteractionSource.interactions.collect { interaction ->
      when (interaction) {
        is androidx.compose.foundation.interaction.DragInteraction.Start -> isSliderInteraction = true
        is androidx.compose.foundation.interaction.DragInteraction.Cancel, is androidx.compose.foundation.interaction.DragInteraction.Stop -> isSliderInteraction = false
      }
    }
  }

  var sliderWidth by remember { mutableFloatStateOf(0f) }
  var sliderOffsetX by remember { mutableIntStateOf(0) }
  val sliderTrackHeight = animateDpAsState(targetValue = if (isSliderInteraction) 38.dp else 10.dp, label = "").value
  val seekTimeTextAlpha = animateFloatAsState(targetValue = if (isSliderInteraction) 1f else 0f, label = "").value
  var seekTimeTextWidth by remember { mutableIntStateOf(0) }

  val sliderValue by remember {
    derivedStateOf {
      when (isSliderInteraction) {
        true -> seekPosition.floatValue
        false -> currentMusicPosition().toFloat()
      }
    }
  }


  Box(
    modifier = modifier,
  ) {
    Column(
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
      androidx.compose.material3.Text(
        modifier = Modifier
          .offset {
            IntOffset(x = sliderOffsetX - (seekTimeTextWidth / 2), y = (-10 - sliderTrackHeight.value.toInt()))
          }
          .alpha(seekTimeTextAlpha)
          .background(Color.White.copy(alpha = 0.2f), androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
          .padding(horizontal = 4.dp)
          .onGloballyPositioned {
            seekTimeTextWidth = it.size.width
          },
        text = sliderValue.convertMilliSecondToTime(),
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
      )
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .onGloballyPositioned {
            sliderWidth = it.size.width.toFloat()
          },
      ) {
        androidx.compose.material3.Slider(
          modifier = Modifier.matchParentSize(),
          value = sliderValue,
          interactionSource = sliderInteractionSource,
          onValueChange = { value ->
            seekPosition.floatValue = value
            sliderOffsetX = ((sliderValue / duration) * sliderWidth).toInt()
          },
          onValueChangeFinished = {
            seekTo.invoke(seekPosition.floatValue.toLong())
          },
          thumb = {},
          track = { sliderState ->
            androidx.compose.material3.SliderDefaults.Track(
              sliderState = sliderState,
              modifier = modifier
                .height(sliderTrackHeight)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(5.dp)),
              colors = androidx.compose.material3.SliderDefaults.colors(
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
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        androidx.compose.material3.Text(
          modifier = Modifier.alpha(1f - seekTimeTextAlpha),
          text = sliderValue.convertMilliSecondToTime(),
          fontSize = 13.sp,
          fontWeight = FontWeight.Normal,
          color = Color.White,
        )
        androidx.compose.material3.Text(
          modifier = Modifier.alpha(1f - seekTimeTextAlpha),
          text = duration.convertMilliSecondToTime(),
          fontSize = 13.sp,
          fontWeight = FontWeight.Normal,
          color = Color.White,
        )
      }

    }
  }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun Preview() {
  MediaPlayerJetpackComposeTheme {
    Column {
      SliderSection(
        modifier = Modifier,
        currentMusicPosition = { 100000 },
        seekTo = {},
        duration = 100000f,
      )
      SliderSection(
        modifier = Modifier,
        currentMusicPosition = { 1000 },
        seekTo = {},
        duration = 100000f,
      )

    }
  }
}