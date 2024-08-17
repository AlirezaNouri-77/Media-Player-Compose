package com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.domain.model.MediaCurrentState
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTimeLine(
  modifier: Modifier = Modifier,
  currentState: () -> MediaCurrentState,
  currentMediaPosition: Int,
  slideValueChange: () -> Unit,
  slideValueChangeFinished: (Float) -> Unit
) {

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .weight(0.1f, false),
      text = currentMediaPosition.convertMilliSecondToTime(),
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium,
      textAlign = TextAlign.Center,
      color = Color.White,
    )
    Slider(
      value = currentMediaPosition.toFloat(),
      modifier = Modifier
        .fillMaxWidth()
        .weight(0.6f)
        .pointerInput(Unit) {
          detectDragGestures { change, dragAmount ->
            change.consume()
            Log.d("TAG8787", "PlayerTimeLine: " + dragAmount)
          }
        },
      onValueChangeFinished = {
        slideValueChangeFinished()
      },
      onValueChange = { value ->
        slideValueChange(value)
      },
      valueRange = 0f..(currentState().metaData.extras?.getInt("DURATION")?.toFloat()
        ?: 0f),
      track = { sliderState ->
        SliderDefaults.Track(
          sliderState = sliderState,
          modifier = Modifier.scale(scaleX = 1f, scaleY = 3f),
          colors = SliderDefaults.colors(
            activeTrackColor = Color.White,
            inactiveTrackColor = Color.White.copy(0.5f),
          ),
        )
      },
      thumb = {},
      colors = SliderDefaults.colors(
        thumbColor = Color.White,
      ),
    )
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .weight(0.1f, false),
      text = (currentState().metaData.extras?.getInt("DURATION") ?: 0f).toInt()
        .convertMilliSecondToTime(),
      fontSize = 13.sp,
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Medium,
      color = Color.White,
    )
  }
}