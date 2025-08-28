package com.example.feature.video.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.ActiveVideoInfo
import com.example.feature.video.R
import com.example.video_media3.model.VideoPlayerState
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun PlayerControllerLayout(
    modifier: Modifier = Modifier,
    playerResizeMode: () -> ContentScale,
    previewSlider: () -> ImageBitmap?,
    currentPlayerState: VideoPlayerState,
    currentPlayerPosition: Long,
    onBackClick: () -> Unit,
    onSeekToPrevious: () -> Unit,
    onSeekToNext: () -> Unit,
    onPausePlayer: () -> Unit,
    onResumePlayer: () -> Unit,
    onSeekToPosition: (Long) -> Unit,
    playerResizeModeChange: () -> Unit,
    getPreviewSlider: (position: Long) -> Unit,
    orientation: Int = LocalConfiguration.current.orientation,
) {

    val controllerPadding = remember(orientation) {
        mutableStateOf(
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                PaddingValues(horizontal = 20.dp)
            } else {
                PaddingValues(horizontal = 15.dp)
            }
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        PlayerTopSection(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.TopCenter),
            onBack = { onBackClick() },
            title = currentPlayerState.currentMediaInfo.title,
        )
        BottomSection(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.BottomCenter),
            previewSlider = previewSlider(),
            onSeekToPrevious = { onSeekToPrevious() },
            onSeekToNext = { onSeekToNext() },
            onPausePlayer = { onPausePlayer() },
            onResumePlayer = { onResumePlayer() },
            onSeekToPosition = { onSeekToPosition(it) },
            playerResizeModeChange = { playerResizeModeChange() },
            currentPlayerState = { currentPlayerState },
            currentPlayerPosition = { currentPlayerPosition },
            playerResizeMode = { playerResizeMode() },
            getPreviewSlider = getPreviewSlider,
        )
    }

}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    previewSlider: ImageBitmap?,
    onSeekToPrevious: () -> Unit,
    onSeekToNext: () -> Unit,
    onPausePlayer: () -> Unit,
    onResumePlayer: () -> Unit,
    onSeekToPosition: (Long) -> Unit,
    playerResizeModeChange: () -> Unit,
    currentPlayerState: () -> VideoPlayerState,
    currentPlayerPosition: () -> Long,
    playerResizeMode: () -> ContentScale,
    getPreviewSlider: (position: Long) -> Unit,
    orientation: Int = LocalConfiguration.current.orientation,
) {
    Card(
        modifier = modifier,
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
                modifier = Modifier.fillMaxWidth(),
                currentMediaDuration = currentPlayerState().currentMediaInfo.duration,
                currentPlayerPosition = currentPlayerPosition(),
                previewSlider = previewSlider,
                onSeekTo = onSeekToPosition,
                getPreviewSlider = getPreviewSlider,
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
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    PlayerControllerButton(
                        icon = if (playerResizeMode() == ContentScale.Fit) R.drawable.iocn_fullscreen_exit_24 else R.drawable.icon_fullscreen_24,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterEnd),
                        onClick = {
                            playerResizeModeChange()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerTopSection(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    title: String,
) {
    Row(
        modifier = modifier
            .background(Color.Transparent)
            .drawBehind {
                drawRoundRect(
                    color = Color.Black,
                    size = this.size,
                    alpha = 0.4f,
                    cornerRadius = CornerRadius(25f, 25f),
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_back_24), contentDescription = "",
            modifier = Modifier
                .weight(0.1f)
                .clickable { onBack() },
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee()
                .padding(vertical = 5.dp)
                .weight(0.9f),
        )
    }
}

@Preview(showSystemUi = true)
@Preview(device = "spec:parent=pixel_5,orientation=landscape", showSystemUi = true)
@Composable
private fun PreviewPlayerControllerLayout() {
    MediaPlayerJetpackComposeTheme {
        PlayerControllerLayout(
            currentPlayerState = VideoPlayerState(
                    currentMediaInfo = ActiveVideoInfo(
                        title = "Example Video",
                        videoID = "",
                        videoUri = "",
                        duration = 240_000
                    ),
                    isPlaying = false,
                    isBuffering = false,
                    repeatMode = 0,
                ),
            onResumePlayer = {},
            onPausePlayer = {},
            onBackClick = {},
            onSeekToNext = {},
            onSeekToPosition = {},
            onSeekToPrevious = {},
            currentPlayerPosition = 0L,
            playerResizeMode = { ContentScale.Fit },
            playerResizeModeChange = {},
            previewSlider = { null },
            getPreviewSlider = {},
        )
    }
}

