package com.example.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.core.common.util.convertMilliSecondToTime
import com.example.core.common.util.removeFileExtension
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme

@Composable
fun MusicMediaItem(
    musicId: Long,
    artworkUri: String,
    name: String,
    artist: String,
    duration: Long,
    isFavorite: Boolean,
    currentMediaId: String,
    isPlaying: () -> Boolean,
    onItemClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick.invoke() }
            .padding(horizontal = 4.dp)
            .background(
                color = if (currentMediaId == musicId.toString()) {
                    MaterialTheme.colorScheme.onPrimary.copy(
                        alpha = 0.2f,
                    )
                } else {
                    Color.Companion.Transparent
                },
                shape = RoundedCornerShape(20.dp),
            ),
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.Companion.padding(vertical = 6.dp, horizontal = 10.dp),
        ) {
            Box(contentAlignment = Alignment.Companion.Center) {
                MusicThumbnail(
                    uri = artworkUri.toUri(),
                    modifier = Modifier.Companion
                        .size(size = 55.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(5.dp))
                        .then(if (currentMediaId == musicId.toString()) Modifier.Companion.blur(5.dp) else Modifier.Companion)
                        .background(color = MaterialTheme.colorScheme.primary),
                )
                if (currentMediaId == musicId.toString()) {
                    WaveForm(
                        modifier = Modifier.Companion
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(5.dp))
                            .background(color = Color.Companion.Black.copy(alpha = 0.4f)),
                        size = 55.dp,
                        enable = isPlaying(),
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.width(10.dp))
            Column(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .weight(0.8f),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    text = name.removeFileExtension(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Companion.Medium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Companion.Ellipsis,
                    softWrap = true,
                )
                Text(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    text = artist,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Companion.Normal,
                    maxLines = 1,
                )
            }
            Column(
                Modifier.Companion.weight(0.2f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.Companion.End,
            ) {
                Text(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    text = duration.convertMilliSecondToTime(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Companion.End,
                )
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                        modifier = Modifier.Companion.size(15.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMusicMediaItem() {
    MediaPlayerJetpackComposeTheme {
        MusicMediaItem(
            musicId = 9586,
            name = "Example Music",
            duration = 1000000,
            artworkUri = "",
            artist = "Example Artist",
            isPlaying = { false },
            currentMediaId = "",
            onItemClick = {},
            isFavorite = true,
        )
    }
}
