package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.shermanrex.core.common.util.convertByteToReadableSize
import com.shermanrex.core.common.util.convertToReadableBitrate
import com.shermanrex.core.common.util.extractFileExtension
import com.shermanrex.core.common.util.removeFileExtension
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.designsystem.util.DeviceSize
import com.shermanrex.core.designsystem.util.calculateWindowSize
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.model.PlayingMusicInfo
import com.shermanrex.core.model.PlayingMusicState
import com.shermanrex.feature.music_player.model.PlayerUiState

@Composable
fun SongDetail(
    modifier: Modifier = Modifier,
    playerUiState: PlayerUiState,
    onArtistClick: (String) -> Unit,
) {
    val windowSize = calculateWindowSize()
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            text = playerUiState.currentPlayerState.playingMusicInfo.title.removeFileExtension(),
            fontSize = if (windowSize == DeviceSize.COMPACT) 20.sp else 14.sp,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Visible,
            color = Color.White,
            maxLines = 1,
        )
        Text(
            modifier = Modifier.clickable { onArtistClick(playerUiState.currentPlayerState.playingMusicInfo.artist) },
            text = playerUiState.currentPlayerState.playingMusicInfo.artist.trim(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            color = Color.White.copy(alpha = 0.7f),
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${playerUiState.currentPlayerState.playingMusicInfo.title.extractFileExtension()}, " +
                "${playerUiState.currentPlayerState.playingMusicInfo.bitrate.convertToReadableBitrate()}, " +
                "${playerUiState.currentPlayerState.playingMusicInfo.size.convertByteToReadableSize()} ",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
        )
    }
}

@Preview
@Composable
private fun FullScreenPreview() {
    MediaPlayerJetpackComposeTheme {
        SongDetail(
            onArtistClick = {},
            playerUiState = PlayerUiState(
                currentPlayerState = PlayingMusicState(
                    playingMusicInfo = PlayingMusicInfo(
                        title = "Blinding Lights.mp3",
                        musicID = "1",
                        artworkUri = "",
                        musicUri = "",
                        artist = "The Weeknd",
                        album = "After Hours",
                        duration = 200000,
                        bitrate = 320,
                        size = 8388608,
                    ),
                    isPlaying = false,
                    isFavorite = true,
                    isBuffering = false,
                    playerRepeatMode = PlayerRepeatMode.MODE_OFF,
                    isShuffleMode = false,
                ),
            ),
        )
    }
}
