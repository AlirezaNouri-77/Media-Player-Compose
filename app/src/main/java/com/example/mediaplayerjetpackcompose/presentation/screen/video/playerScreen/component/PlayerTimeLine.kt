package com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.util.Constant
import com.example.mediaplayerjetpackcompose.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.core.model.MediaPlayerState
import com.example.mediaplayerjetpackcompose.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTimeLine(
  modifier: Modifier = Modifier,
  slidePosition: () -> Float,
  currentState: () -> MediaPlayerState,
  currentMediaPosition: Int,
  slideValueChange: (Float) -> Unit,
  slideValueChangeFinished: () -> Unit,
) {

  ConstraintLayout(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
  ) {
    val (sliderRef, durationRef, currentPositionRef) = createRefs()

    val endGuild = createGuidelineFromEnd(10.dp)
    val startGuild = createGuidelineFromStart(10.dp)

    Slider(
      modifier = modifier
        .fillMaxWidth()
        .constrainAs(sliderRef) {
          top.linkTo(parent.top)
          end.linkTo(parent.end)
          start.linkTo(parent.start)
        },
      value = slidePosition(),
      onValueChangeFinished = {
        slideValueChangeFinished()
      },
      onValueChange = { value ->
        slideValueChange(value)
      },
      valueRange = 0f..(currentState().metaData.extras?.getInt(Constant.DURATION_KEY)?.toFloat()
        ?: 0f),

      track = { sliderState ->
        SliderDefaults.Track(
          sliderState = sliderState,
          modifier = Modifier.graphicsLayer {
            scaleY = 0.6f
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

    Text(
      modifier = Modifier.constrainAs(currentPositionRef) {
        top.linkTo(sliderRef.bottom, margin = 3.dp)
        bottom.linkTo(sliderRef.bottom)
        start.linkTo(startGuild)
      },
      text = currentMediaPosition.convertMilliSecondToTime(),
      fontSize = 15.sp,
      fontWeight = FontWeight.Medium,
      color = Color.White,
    )

    Text(
      modifier = Modifier.constrainAs(durationRef) {
        top.linkTo(sliderRef.bottom, margin = 3.dp)
        bottom.linkTo(sliderRef.bottom)
        end.linkTo(endGuild)
      },
      text = (currentState().metaData.extras?.getInt(Constant.DURATION_KEY) ?: 0f).toInt()
        .convertMilliSecondToTime(),
      fontSize = 15.sp,
      fontWeight = FontWeight.Medium,
      color = Color.White,
    )

  }

}

@Preview
@Composable
private fun PreviewPlayerTimeLine() {
  MediaPlayerJetpackComposeTheme {
    PlayerTimeLine(
      currentState = { MediaPlayerState.Empty },
      currentMediaPosition = 0,
      slideValueChange = {},
      slidePosition = { 0f },
      slideValueChangeFinished = {},
    )
  }
}