package com.example.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.R
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.music_media3.model.PlayerStateModel
import com.example.core.music_media3.model.RepeatModes

@Composable
fun SongController(
    modifier: Modifier = Modifier,
    playerStateModel: () -> PlayerStateModel,
    isFavorite: Boolean,
    repeatMode: Int,
    onMovePreviousMusic: () -> Unit,
    onPauseMusic: () -> Unit,
    onResumeMusic: () -> Unit,
    onMoveNextMusic: () -> Unit,
    onRepeatMode: (Int) -> Unit,
    onFavoriteToggle: () -> Unit,
) {
    val favIcon = remember(playerStateModel().currentMediaInfo.musicID, isFavorite) {
        when (isFavorite) {
            true -> Icons.Default.Favorite
            false -> Icons.Default.FavoriteBorder
        }
    }

    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ButtonOfFullScreenPlayer(
            icon = favIcon,
            size = DpSize(24.dp, 24.dp),
            contentDescription = "Fav Music",
            onClick = { onFavoriteToggle.invoke() },
        )
        ButtonOfFullScreenPlayer(
            icon = R.drawable.icon_previous,
            size = DpSize(26.dp, 26.dp),
            contentDescription = "Next",
            onClick = onMovePreviousMusic,
        )
        ButtonOfFullScreenPlayer(
            modifier = Modifier.padding(horizontal = 5.dp),
            icon = if (playerStateModel().isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
            size = DpSize(48.dp, 48.dp),
            contentDescription = "Play and Pause",
            onClick = {
                when (playerStateModel().isPlaying) {
                    true -> onPauseMusic.invoke()
                    false -> onResumeMusic.invoke()
                }
            },
        )
        ButtonOfFullScreenPlayer(
            icon = R.drawable.icon_next,
            size = DpSize(26.dp, 26.dp),
            contentDescription = "Next",
            onClick = onMoveNextMusic,
        )
        ButtonOfFullScreenPlayer(
            icon = when (repeatMode) {
                0 -> R.drawable.icon_repeat_off
                1 -> R.drawable.icon_repeat_once
                2 -> R.drawable.icon_repeat_all
                else -> -1
            },
            size = DpSize(24.dp, 24.dp),
            contentDescription = "RepeatMode",
            onClick = {
                if (repeatMode == RepeatModes.entries.toMutableList().lastIndex) {
                    onRepeatMode.invoke(0)
                } else {
                    onRepeatMode.invoke(repeatMode + 1)
                }
            },
        )
    }
}

@Composable
private fun ButtonOfFullScreenPlayer(
    modifier: Modifier = Modifier,
    icon: Any,
    size: DpSize,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier.size(size),
        onClick = { onClick.invoke() },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
        ),
    ) {
        if (icon is Int) {
            Icon(
                modifier = Modifier.fillMaxSize(0.9f),
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = contentDescription,
            )
        } else if (icon is ImageVector) {
            Icon(
                modifier = Modifier.fillMaxSize(0.9f),
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlayerJetpackComposeTheme {
        SongController(
            modifier = Modifier,
            playerStateModel = { PlayerStateModel.Empty },
            isFavorite = false,
            repeatMode = 0,
            onMovePreviousMusic = {},
            onPauseMusic = {},
            onResumeMusic = {},
            onMoveNextMusic = {},
            onRepeatMode = {},
            onFavoriteToggle = {},
        )
    }
}
