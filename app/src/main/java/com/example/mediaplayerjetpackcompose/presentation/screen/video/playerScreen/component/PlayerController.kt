package com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.core.model.MediaPlayerState
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun PlayerController(
  modifier: Modifier = Modifier,
  currentState: () -> MediaPlayerState,
  onSeekToPrevious: () -> Unit,
  onSeekToNext: () -> Unit,
  onPause: () -> Unit,
  onResume: () -> Unit,
) {

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
  ) {
    PlayerControllerButton(
      icon = R.drawable.icon_previous,
      modifier = Modifier
        .size(25.dp),
      onClick = {
        onSeekToPrevious()
      },
    )
    AnimatedContent(
      targetState = if (currentState().isPlaying || currentState().isBuffering) R.drawable.icon_pause else R.drawable.icon_play,
      label = "",
    ) {
      PlayerControllerButton(
        icon = it,
        modifier = Modifier.size(30.dp),
        onClick = {
          when (currentState().isPlaying) {
            true -> onPause()
            false -> onResume()
          }
        },
      )
    }
    PlayerControllerButton(
      icon = R.drawable.icon_next,
      modifier = Modifier.size(25.dp),
      onClick = {
        onSeekToNext()
      },
    )
  }

}

@Preview
@Composable
private fun PreviewPlayerController() {
  MediaPlayerJetpackComposeTheme {
    PlayerController(
      currentState = { MediaPlayerState.Empty },
      onSeekToPrevious = {},
      onSeekToNext = {},
      onPause = {},
      onResume = {}
    )
  }
}