package com.example.feature.music_player.fullScreen.component

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
import com.example.core.common.util.convertByteToReadableSize
import com.example.core.common.util.convertToReadableBitrate
import com.example.core.common.util.extractFileExtension
import com.example.core.common.util.removeFileExtension
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.ActiveMusicInfo
import com.example.core.model.PlayerStateModel

@Composable
fun SongDetail(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    currentPlayerStateModel: PlayerStateModel,
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
                text = currentPlayerStateModel.currentMediaInfo.title.removeFileExtension(),
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
            modifier = Modifier.clickable { onArtistClick(currentPlayerStateModel.currentMediaInfo.artist) },
            text = currentPlayerStateModel.currentMediaInfo.artist,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            color = Color.White.copy(alpha = 0.7f),
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${currentPlayerStateModel.currentMediaInfo.title.extractFileExtension()}, " +
                "${currentPlayerStateModel.currentMediaInfo.bitrate.convertToReadableBitrate()}, " +
                "${currentPlayerStateModel.currentMediaInfo.size.convertByteToReadableSize()} ",
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
            currentPlayerStateModel = PlayerStateModel(
                currentMediaInfo = ActiveMusicInfo(
                    title = "Blinding Lights.mp3",
                    musicID = "1",
                    artworkUri = "",
                    musicUri = "",
                    artist = "The Weeknd",
                    album = "After Hours",
                    duration = 200000,
                    bitrate = 320,
                    size = 8388608,
                    isFavorite = true,
                ),
            ),
        )
    }
}
