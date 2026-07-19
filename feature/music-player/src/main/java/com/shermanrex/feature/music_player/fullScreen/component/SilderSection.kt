package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shermanrex.core.common.util.convertMilliSecondToTime
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderSection(
    modifier: Modifier = Modifier,
    currentMusicPosition: Long,
    seekTo: (Long) -> Unit,
    duration: Float,
) {
    var seekPosition by remember { mutableFloatStateOf(0f) }
    val sliderInteractionSource = remember { MutableInteractionSource() }
    val isSliderDragging by sliderInteractionSource.collectIsDraggedAsState()
    val thumbWidth by animateDpAsState(targetValue = if (isSliderDragging) 14.dp else 12.dp, label = "")

    val sliderValue by remember(currentMusicPosition) {
        derivedStateOf {
            when (isSliderDragging) {
                true -> seekPosition
                false -> currentMusicPosition.toFloat()
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        Slider(
            modifier = Modifier.height(16.dp),
            value = sliderValue,
            interactionSource = sliderInteractionSource,
            onValueChange = { value -> seekPosition = value },
            onValueChangeFinished = { seekTo(seekPosition.toLong()) },
            thumb = {
                SliderDefaults.Thumb(
                    modifier = Modifier.offset(y = 1.5.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                    ),
                    thumbSize = DpSize(thumbWidth, thumbWidth),
                    interactionSource = remember { MutableInteractionSource() },
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.height(4.dp),
                    sliderState = sliderState,
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.White.copy(alpha = 0.8f),
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f),
                    ),
                    drawStopIndicator = null,
                    trackInsideCornerSize = 0.dp,
                    thumbTrackGapSize = 0.dp,
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
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
            )
            Text(
                text = duration.convertMilliSecondToTime(),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
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
                currentMusicPosition = 100000,
                seekTo = {},
                duration = 100000f,
            )
            SliderSection(
                modifier = Modifier,
                currentMusicPosition = 1000,
                seekTo = {},
                duration = 100000f,
            )
            SliderSection(
                modifier = Modifier,
                currentMusicPosition = 10000,
                seekTo = {},
                duration = 100000f,
            )
        }
    }
}
