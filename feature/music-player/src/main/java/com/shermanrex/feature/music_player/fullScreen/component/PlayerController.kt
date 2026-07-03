package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.shermanrex.core.designsystem.R
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.model.PlayerRepeatMode

@Composable
fun SongController(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    isShuffleMode: Boolean,
    playerRepeatMode: PlayerRepeatMode,
    onMovePreviousMusic: () -> Unit,
    onPauseMusic: () -> Unit,
    onResumeMusic: () -> Unit,
    onMoveNextMusic: () -> Unit,
    onRepeatMode: (PlayerRepeatMode) -> Unit,
    onShuffleModeClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ButtonOfFullScreenPlayer(
            icon = R.drawable.icon_shuffle,
            color = if (isShuffleMode) Color.White else Color.White.copy(alpha = 0.5f),
            contentDescription = "shuffle Music",
            onClick = onShuffleModeClick,
        )
        ButtonOfFullScreenPlayer(
            icon = R.drawable.icon_previous,
            size = 32.dp,
            contentDescription = "Next",
            onClick = onMovePreviousMusic,
        )
        ButtonOfFullScreenPlayer(
            icon = if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
            size = 42.dp,
            contentDescription = "Play and Pause",
            onClick = {
                when (isPlaying) {
                    true -> onPauseMusic.invoke()
                    false -> onResumeMusic.invoke()
                }
            },
        )
        ButtonOfFullScreenPlayer(
            icon = R.drawable.icon_next,
            size = 32.dp,
            contentDescription = "Next",
            onClick = onMoveNextMusic,
        )
        ButtonOfFullScreenPlayer(
            icon = when (playerRepeatMode) {
                PlayerRepeatMode.MODE_OFF -> R.drawable.icon_repeat_off
                PlayerRepeatMode.MODE_ONE -> R.drawable.icon_repeat_once
                PlayerRepeatMode.MODE_ALL -> R.drawable.icon_repeat_all
            },
            contentDescription = "RepeatMode",
            color = if (playerRepeatMode != PlayerRepeatMode.MODE_OFF) {
                Color.White
            } else {
                Color.White.copy(
                    alpha = 0.5f,
                )
            },
            onClick = {
                when (playerRepeatMode) {
                    PlayerRepeatMode.MODE_OFF -> onRepeatMode.invoke(PlayerRepeatMode.MODE_ONE)
                    PlayerRepeatMode.MODE_ONE -> onRepeatMode.invoke(PlayerRepeatMode.MODE_ALL)
                    PlayerRepeatMode.MODE_ALL -> onRepeatMode.invoke(PlayerRepeatMode.MODE_OFF)
                }
            },
        )
    }
}

@Composable
private fun ButtonOfFullScreenPlayer(
    modifier: Modifier = Modifier,
    icon: Int,
    size: Dp = 24.dp,
    color: Color = Color.White,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier.size(size + 16.dp),
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = color,
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun ButtonWithBounceEffect(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    size: DpSize,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(if (isPressed) 0.85f else 1f)

    IconButton(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                this.scaleX *= scale
                this.scaleY *= scale
            },
        onClick = onClick,
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
        ),
    ) {
        content()
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlayerJetpackComposeTheme {
        SongController(
            modifier = Modifier,
            isPlaying = false,
            isShuffleMode = true,
            playerRepeatMode = PlayerRepeatMode.MODE_OFF,
            onMovePreviousMusic = {},
            onPauseMusic = {},
            onResumeMusic = {},
            onMoveNextMusic = {},
            onRepeatMode = {},
            onShuffleModeClick = {},
        )
    }
}
