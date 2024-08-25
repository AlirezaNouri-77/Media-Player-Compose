package com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState

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
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    PlayerControllerButton(
      icon = R.drawable.icon_skip_previous_24,
      modifier = Modifier
        .size(24.dp),
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
      icon = R.drawable.icon_skip_next_24,
      modifier = Modifier.size(24.dp),
      onClick = {
        onSeekToNext()
      },
    )
  }

}