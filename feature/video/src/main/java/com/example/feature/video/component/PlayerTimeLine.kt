package com.example.feature.video.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.common.util.convertMilliSecondToTime
import com.example.core.designsystem.NoRippleEffect
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun PlayerTimeLine(
  modifier: Modifier = Modifier,
  currentMediaDuration: Long,
  currentPlayerPosition: Long,
  onSeekTo: (Long) -> Unit,
  getPreviewSlider: (position: Long) -> Unit,
  previewSlider: ImageBitmap?,
) {

  var draggingSliderPosition by rememberSaveable {
    mutableLongStateOf(0L)
  }

  val sliderInteraction = remember { MutableInteractionSource() }
  val isSliderDragging by sliderInteraction.collectIsDraggedAsState()

  val sliderPosition = remember(draggingSliderPosition,currentPlayerPosition) {
    if (isSliderDragging) draggingSliderPosition else currentPlayerPosition
  }

  LaunchedEffect(key1 = draggingSliderPosition) {
    snapshotFlow {
      draggingSliderPosition
    }.debounce(100L)
      .distinctUntilChanged()
      .collectLatest {
        getPreviewSlider(it)
      }
  }

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    PlayerTimeLinePreview(
      isVisible = previewSlider != null && isSliderDragging,
      previewBitmap = previewSlider,
      videoPosition = sliderPosition,
    )
    Slider(
      modifier = modifier.fillMaxWidth(),
      value = sliderPosition.toFloat(),
      interactionSource = sliderInteraction,
      onValueChangeFinished = { onSeekTo(draggingSliderPosition) },
      onValueChange = { value -> draggingSliderPosition = value.toLong() },
      valueRange = 0f..currentMediaDuration.toFloat(),
      track = { sliderState ->
        SliderDefaults.Track(
          sliderState = sliderState,
          modifier = Modifier.graphicsLayer {
            scaleY = 0.7f
          },
          drawStopIndicator = {},
          colors = SliderDefaults.colors(
            activeTrackColor = Color.White.copy(alpha = 0.9f),
            inactiveTrackColor = Color.White.copy(alpha = 0.6f),
          ),
          thumbTrackGapSize = 4.dp,
          trackInsideCornerSize = 2.dp,
        )
      },
      thumb = {
        SliderDefaults.Thumb(
          thumbSize = DpSize(width = 4.dp, height = 25.dp),
          interactionSource = NoRippleEffect,
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
          ),
        )
      }
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
      Text(
        text = currentPlayerPosition.convertMilliSecondToTime(),
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
      )

      Text(
        text = currentMediaDuration.toInt().convertMilliSecondToTime(),
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
      )
    }

  }

}

@Preview
@Composable
private fun PreviewPlayerTimeLine() {
  MediaPlayerJetpackComposeTheme {
    PlayerTimeLine(
        currentPlayerPosition = 0,
        onSeekTo = {},
        previewSlider = null,
        getPreviewSlider = {},
        currentMediaDuration = 10000,
    )
  }
}