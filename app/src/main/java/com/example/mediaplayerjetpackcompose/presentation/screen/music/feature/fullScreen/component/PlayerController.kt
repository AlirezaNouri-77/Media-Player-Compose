package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.util.Constant
import com.example.mediaplayerjetpackcompose.core.model.MediaPlayerState
import com.example.mediaplayerjetpackcompose.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun SongController(
  modifier: Modifier = Modifier,
  mediaPlayerState: () -> MediaPlayerState,
  isFavorite: Boolean,
  repeatMode: Int,
  onMovePreviousMusic: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
  onMoveNextMusic: () -> Unit,
  onRepeatMode: (Int) -> Unit,
  onFavoriteToggle: () -> Unit,
) {

  val favIcon = remember(mediaPlayerState().mediaId, isFavorite) {
    when (isFavorite) {
      true -> Icons.Default.Favorite
      false -> Icons.Default.FavoriteBorder
    }
  }

  Row(
    modifier = modifier.fillMaxWidth(),
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
      onClick = {
        onMovePreviousMusic.invoke()
      },
    )
    ButtonOfFullScreenPlayer(
      modifier = Modifier.padding(horizontal = 5.dp),
      icon = if (mediaPlayerState().isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
      size = DpSize(42.dp, 42.dp),
      contentDescription = "Play and Pause",
      onClick = {
        when (mediaPlayerState().isPlaying) {
          true -> onPauseMusic.invoke()
          false -> onResumeMusic.invoke()
        }
      }
    )
    ButtonOfFullScreenPlayer(
      icon = R.drawable.icon_next,
      size = DpSize(26.dp, 26.dp),
      contentDescription = "Next",
      onClick = {
        onMoveNextMusic.invoke()
      },
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
        if (repeatMode == Constant.RepeatModes.entries.toMutableList().lastIndex) {
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
    interactionSource = NoRippleEffect,
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
      mediaPlayerState = { MediaPlayerState.Empty },
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
