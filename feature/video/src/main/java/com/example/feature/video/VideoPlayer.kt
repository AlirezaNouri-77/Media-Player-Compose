package com.example.feature.video

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import com.example.feature.video.component.MiddleInfoHandler
import com.example.feature.video.component.PlayerControllerLayout
import com.example.feature.video.model.VideoPlayerOverlayState
import com.example.feature.video.util.hideSystemBars
import com.example.feature.video.util.showSystemBars

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUri: String = "",
    videoViewModel: VideoViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onBack: () -> Unit = {},
) {
    val window = LocalActivity.current?.window
    val previewSliderBitmap by videoViewModel.previewSliderBitmap.collectAsStateWithLifecycle(null)

    val uiState by videoViewModel.uiState.collectAsStateWithLifecycle()

    val onBack: () -> Unit = {
        videoViewModel.stopPlayer()
        showSystemBars(window)
        onBack()
    }

    LaunchedEffect(uiState.isVideoPlayerOverlayControllerVisible) {
        if (uiState.isVideoPlayerOverlayControllerVisible) showSystemBars(window) else hideSystemBars(window)
    }

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observe = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> videoViewModel.resumePlayer()
                Lifecycle.Event.ON_STOP -> videoViewModel.pausePlayer()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observe)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observe)
        }
    })

    BackHandler {
        onBack()
    }

    LaunchedEffect(
        key1 = videoUri,
        block = {
            if (videoUri.isNotEmpty()) {
                videoViewModel.startPlayFromUri(videoUri.toUri())
            }
        },
    )

    val playerContentScale = remember { mutableStateOf(ContentScale.Fit) }
    val presentationState = rememberPresentationState(videoViewModel.getExoPlayer)
    val scaledModifier = Modifier.resizeWithContentScale(playerContentScale.value, presentationState.videoSizeDp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .playerViewTapHandler(
                onTapClick = {
                    videoViewModel.setVideoPlayerOverlayControllerVisibility(!uiState.isVideoPlayerOverlayControllerVisible)
                },
                onRightDoubleClick = {
                    videoViewModel.fastForward(15_000, uiState.currentPlayerPosition)
                    videoViewModel.updateMiddleVideoPlayerInfo(VideoPlayerOverlayState.FastForward())
                },
                onLeftDoubleClick = {
                    videoViewModel.fastRewind(15_000, uiState.currentPlayerPosition)
                    videoViewModel.updateMiddleVideoPlayerInfo(VideoPlayerOverlayState.FastRewind())
                },
            ),
    ) {
        PlayerSurface(
            player = videoViewModel.getExoPlayer,
            modifier = scaledModifier,
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        )

        if (presentationState.coverSurface) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
            )
        }
    }

    AnimatedVisibility(
        visible = uiState.isVideoPlayerOverlayControllerVisible,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        PlayerControllerLayout(
            playerResizeMode = { playerContentScale.value },
            previewSlider = { previewSliderBitmap },
            currentPlayerState = uiState.videoPlayerState,
            currentPlayerPosition = uiState.currentPlayerPosition,
            getPreviewSlider = videoViewModel::getSliderPreviewThumbnail,
            onSeekToPrevious = videoViewModel::seekToPrevious,
            onSeekToNext = videoViewModel::seekToNext,
            onPausePlayer = videoViewModel::pausePlayer,
            onResumePlayer = videoViewModel::resumePlayer,
            onSeekToPosition = videoViewModel::seekToPosition,
            onBackClick = onBack,
            playerResizeModeChange = {
                playerContentScale.value =
                    if (playerContentScale.value == ContentScale.Fit) ContentScale.FillWidth else ContentScale.Fit
            },
        )
    }

    // show a info when fastforwar or fastrewind triggered
    MiddleInfoHandler(
        modifier = Modifier,
        showInfoMiddleScreen = uiState.videoPlayerOverlayState != VideoPlayerOverlayState.Initial,
        videoPlayerOverlayState = uiState.videoPlayerOverlayState,
    )
}

private fun Modifier.playerViewTapHandler(
    onTapClick: () -> Unit,
    onRightDoubleClick: () -> Unit,
    onLeftDoubleClick: () -> Unit,
): Modifier = this.then(
    Modifier.pointerInput(null) {
        detectTapGestures(
            onTap = { onTapClick() },
            onDoubleTap = { offset ->
                when {
                    offset.x > this.size.width / 2 -> onRightDoubleClick()
                    offset.x < this.size.width / 2 -> onLeftDoubleClick()
                }
            },
        )
    },
)
