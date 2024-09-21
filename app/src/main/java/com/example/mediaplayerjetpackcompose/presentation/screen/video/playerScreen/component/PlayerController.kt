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
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun PlayerController(
  modifier: Modifier = Modifier,
  currentState: () -> CurrentMediaState,
  onSeekToPrevious: () -> Unit,
  onSeekToNext: () -> Unit,
  onPause: () -> Unit,
  onResume: () -> Unit,
) {

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
  ) {
    PlayerControllerButton(
      icon = R.drawable.icon_skip_previous_24,
      modifier = Modifier
        .size(35.dp),
      onClick = {
        onSeekToPrevious()
      },
    )
    AnimatedContent(
      targetState = if (currentState().isPlaying) R.drawable.icon_pause_24 else R.drawable.icon_play_arrow_24,
      label = "",
    ) {
      PlayerControllerButton(
        icon = it,
        modifier = Modifier.size(45.dp),
        onClick = {
          when (currentState().isPlaying) {
            true -> onPause()
            false -> onResume()
          }
        },
      )
    }
    PlayerControllerButton(
      icon = R.drawable.icon_skip_next_24,
      modifier = Modifier.size(35.dp),
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
      currentState = { CurrentMediaState.Empty },
      onSeekToPrevious = {},
      onSeekToNext = {},
      onPause = {},
      onResume = {}
    )
  }
}