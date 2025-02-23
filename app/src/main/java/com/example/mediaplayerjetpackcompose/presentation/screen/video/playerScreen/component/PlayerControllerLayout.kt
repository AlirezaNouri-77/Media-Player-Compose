package com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component

import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.core.model.MediaPlayerState
import com.example.mediaplayerjetpackcompose.core.model.MiddleVideoPlayerIndicator
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme


@OptIn(UnstableApi::class)
@Composable
fun PlayerControllerLayout(
  modifier: Modifier = Modifier,
  isVisible: Boolean,
  controllerLayoutPadding: PaddingValues,
  currentDeviceOrientation: Int,
  playerResizeMode: Int,
  previewSlider: ImageBitmap?,
  currentPlayerState: () -> MediaPlayerState,
  currentPlayerPosition: () -> Long,
  onBackClick: () -> Unit,
  onSeekToPrevious: () -> Unit,
  onSeekToNext: () -> Unit,
  onPausePlayer: () -> Unit,
  onResumePlayer: () -> Unit,
  onSeekToPosition: (Long) -> Unit,
  slideSeekPosition: () -> Float,
  slidePositionChange: (Float) -> Unit,
  playerResizeModeChange: (Int) -> Unit,
  onMiddleVideoPlayerIndicator: (MiddleVideoPlayerIndicator) -> Unit,
) {

  var isSliderInteraction by remember {
    mutableStateOf(false)
  }

  val sliderValue = if (isSliderInteraction) slideSeekPosition() else currentPlayerPosition().toFloat()

  AnimatedVisibility(
    modifier = modifier.fillMaxSize(),
    visible = isVisible,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {

    ConstraintLayout(
      modifier = Modifier
        .fillMaxSize()
        .padding(controllerLayoutPadding)
        .then(
          if (currentDeviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Modifier
              .navigationBarsPadding()
              .displayCutoutPadding()
          } else Modifier
        ),
    ) {
      val (topSec, bottomSec, sliderThumbnailPreview) = createRefs()

      PlayerTimeLinePreview(
        modifier = Modifier
          .constrainAs(sliderThumbnailPreview) {
            bottom.linkTo(bottomSec.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          },
        shouldShow = previewSlider != null && isSliderInteraction,
        previewBitmap = previewSlider,
        videoPosition = slideSeekPosition().toInt()
      )

      PlayerTopSection(
        modifier = Modifier.constrainAs(topSec) {
          top.linkTo(parent.top, margin = 10.dp)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        },
        controllerLayoutPadding = controllerLayoutPadding,
        onBack = { onBackClick() },
        title = currentPlayerState().metaData.title?.removeFileExtension() ?: "-",
      )

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .constrainAs(bottomSec) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom, margin = 10.dp)
          },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color.Black.copy(alpha = 0.5f),
          contentColor = Color.White,
        ),
      ) {

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {

          PlayerTimeLine(
            modifier = Modifier
              .fillMaxWidth(),
            currentState = { currentPlayerState() },
            currentMediaPosition = currentPlayerPosition().toInt(),
            slidePosition = { sliderValue },
            slideValueChangeFinished = {
              isSliderInteraction = false
              onSeekToPosition(slideSeekPosition().toLong())
            },
            slideValueChange = { slideValue ->
              onMiddleVideoPlayerIndicator(MiddleVideoPlayerIndicator.Seek(slideValue.toLong()))
              slidePositionChange(slideValue)
              isSliderInteraction = true
            },
          )

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center,
          ) {

            PlayerController(
              modifier = Modifier,
              currentState = { currentPlayerState() },
              onSeekToPrevious = { onSeekToPrevious() },
              onSeekToNext = { onSeekToNext() },
              onPause = { onPausePlayer() },
              onResume = { onResumePlayer() },
            )

            if (currentDeviceOrientation == Configuration.ORIENTATION_LANDSCAPE) {
              var iconResizeMode = remember {
                when (playerResizeMode) {
                  AspectRatioFrameLayout.RESIZE_MODE_FILL -> R.drawable.icon_fullscreen_24
                  AspectRatioFrameLayout.RESIZE_MODE_FIT -> R.drawable.iocn_fullscreen_exit_24
                  else -> 0
                }
              }
              PlayerControllerButton(
                icon = iconResizeMode,
                modifier = Modifier
                  .size(30.dp)
                  .align(Alignment.CenterEnd),
                onClick = {
                  if (playerResizeMode == AspectRatioFrameLayout.RESIZE_MODE_FILL) {
                    playerResizeModeChange(AspectRatioFrameLayout.RESIZE_MODE_FIT)
                  } else {
                    playerResizeModeChange(AspectRatioFrameLayout.RESIZE_MODE_FILL)
                    iconResizeMode = R.drawable.iocn_fullscreen_exit_24
                  }
                },
              )
            }

          }
        }

      }

    }
  }

}

@Preview
@Preview(widthDp = 700, heightDp = 500)
@Composable
private fun PreviewPlayerControllerLayout() {
  MediaPlayerJetpackComposeTheme {
    PlayerControllerLayout(
      isVisible = true,
      currentPlayerState = { MediaPlayerState.Empty },
      onResumePlayer = {},
      onPausePlayer = {},
      onBackClick = {},
      onSeekToNext = {},
      onSeekToPosition = {},
      onSeekToPrevious = {},
      onMiddleVideoPlayerIndicator = {},
      currentPlayerPosition = { 0L },
      playerResizeMode = 0,
      slidePositionChange = {},
      slideSeekPosition = { 0f },
      currentDeviceOrientation = 2,
      playerResizeModeChange = {},
      previewSlider = null,
      controllerLayoutPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp),
    )
  }
}

