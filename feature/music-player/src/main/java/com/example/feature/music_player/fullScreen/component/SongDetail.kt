package com.example.feature.music_player.fullScreen.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.core.common.util.convertByteToReadableSize
import com.example.core.common.util.convertToReadableBitrate
import com.example.core.common.util.extractFileExtension
import com.example.core.common.util.removeFileExtension
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.PlayerStateModel

@Composable
fun SongDetail(
    modifier: Modifier = Modifier,
    currentPlayerStateModel: PlayerStateModel,
    clickOnArtist: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            text = currentPlayerStateModel.currentMediaInfo.title.removeFileExtension(),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Visible,
            color = Color.White,
            maxLines = 1,
        )
        Text(
            modifier = Modifier
                .clickable { clickOnArtist(currentPlayerStateModel.currentMediaInfo.artist) },
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

@androidx.compose.ui.tooling.preview.Preview()
@Composable
private fun FullScreenPreview() {
    MediaPlayerJetpackComposeTheme {
        SongDetail(
            currentPlayerStateModel = PlayerStateModel.Initial,
            clickOnArtist = {},
        )
    }
}
