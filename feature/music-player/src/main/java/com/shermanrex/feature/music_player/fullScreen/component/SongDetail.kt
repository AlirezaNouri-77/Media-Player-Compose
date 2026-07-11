package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shermanrex.core.common.util.convertByteToReadableSize
import com.shermanrex.core.common.util.convertToReadableBitrate
import com.shermanrex.core.common.util.extractFileExtension
import com.shermanrex.core.common.util.removeFileExtension
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.model.PlayingMusicInfo
import com.shermanrex.core.model.PlayingMusicState
import com.shermanrex.feature.music_player.model.PlayerUiState

@Composable
fun SongDetail(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    playerUiState: PlayerUiState,
    onArtistClick: (String) -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee()
                    .weight(1f),
                text = playerUiState.currentPlayerState.playingMusicInfo.title.removeFileExtension(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Visible,
                color = Color.White,
                maxLines = 1,
            )
            IconButton(
                onClick = onFavoriteClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White.copy(0.8f),
                ),
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "",
                )
            }
        }
        Text(
            modifier = Modifier.clickable { onArtistClick(playerUiState.currentPlayerState.playingMusicInfo.artist) },
            text = playerUiState.currentPlayerState.playingMusicInfo.artist,
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

@Preview()
@Composable
private fun FullScreenPreview() {
    MediaPlayerJetpackComposeTheme {
        SongDetail(
            onArtistClick = {},
            isFavorite = true,
            onFavoriteClick = {},
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
