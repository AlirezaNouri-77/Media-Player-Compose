package com.example.feature.music_player.fullScreen.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.common.util.convertMilliSecondToTime
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderSection(
    modifier: Modifier = Modifier,
    currentMusicPosition: () -> Long,
    seekTo: (Long) -> Unit,
    duration: Float,
) {
    var seekPosition by remember {
        mutableFloatStateOf(0f)
    }

    val sliderInteractionSource = remember { MutableInteractionSource() }
    val isSliderDragging by sliderInteractionSource.collectIsDraggedAsState()

    val trackHeight by animateDpAsState(targetValue = if (isSliderDragging) 16.dp else 20.dp, label = "")
    val thumbWidth by animateDpAsState(targetValue = if (isSliderDragging) 4.dp else 6.dp, label = "")

    val sliderValue by remember {
        derivedStateOf {
            when (isSliderDragging) {
                true -> seekPosition
                false -> currentMusicPosition().toFloat()
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        Slider(
            modifier = Modifier.wrapContentHeight(),
            value = sliderValue,
            interactionSource = sliderInteractionSource,
            onValueChange = { value -> seekPosition = value },
            onValueChangeFinished = { seekTo.invoke(seekPosition.toLong()) },
            thumb = {
                SliderDefaults.Thumb(
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                    ),
                    thumbSize = DpSize(width = thumbWidth, height = 26.dp),
                    interactionSource = remember { MutableInteractionSource() },
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.height(trackHeight),
                    sliderState = sliderState,
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.5f),
                    ),
                    drawStopIndicator = null,
                    trackInsideCornerSize = 4.dp,
                    thumbTrackGapSize = 3.dp,
                )
            },
            valueRange = 0f..duration,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = sliderValue.convertMilliSecondToTime(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            )
            Text(
                text = duration.convertMilliSecondToTime(),
                fontSize = 14.sp,
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
            SliderSection(
                modifier = Modifier,
                currentMusicPosition = { 10000 },
                seekTo = {},
                duration = 100000f,
            )
        }
    }
}
