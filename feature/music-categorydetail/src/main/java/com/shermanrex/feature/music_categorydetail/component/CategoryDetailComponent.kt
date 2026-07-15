package com.shermanrex.feature.music_categorydetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shermanrex.core.designsystem.music.MusicListComponent
import com.shermanrex.core.designsystem.music.MusicThumbnail
import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.model.PlayingMusicInfo
import com.shermanrex.core.model.PlayingMusicState
import com.shermanrex.feature.music_categorydetail.CategoryUiState

@Composable
fun SharedTransitionScope.CategoryDetailComponent(
    modifier: Modifier = Modifier,
    categoryUiState: CategoryUiState,
    categoryName: String,
    onMusicClick: (index: Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    showArtworkOnTop: Boolean = true,
) {
    if (categoryUiState.songList.isNotEmpty()) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (showArtworkOnTop) {
                    MusicThumbnail(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        uri = categoryUiState.songList.firstOrNull { it.artworkUri.isNotEmpty() }?.artworkUri,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                renderInOverlayDuringTransition = false,
                            )
                            .basicMarquee(),
                        text = categoryName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = categoryUiState.detail,
                        fontSize = 16.sp,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            MusicListComponent(
                listItem = categoryUiState.songList,
                isPlaying = categoryUiState.playingMusicState.isPlaying,
                currentMusicId = categoryUiState.playingMusicState.playingMusicInfo.musicID,
                onMusicClick = { index, _ -> onMusicClick(index) },
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    name = "Phone - Portrait",
    device = "spec:width=411dp,height=891dp,orientation=portrait",
    showBackground = true,
)
@Preview(
    name = "Tablet - Landscape",
    device = "spec:width=1280dp,height=800dp,orientation=landscape",
    showBackground = true,
)
@Composable
fun CategoryDetailComponentPreview() {
    val dummyMusicList = listOf(
        MusicModel(
            musicId = 102,
            uri = "content://media/external/audio/media/102",
            path = "/storage/emulated/0/Music/neon_horizon.mp3",
            mimeTypes = "audio/mpeg",
            name = "Neon Horizon",
            duration = 252000,
            size = 10485760,
            artworkUri = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=250",
            bitrate = 320,
            artist = "Synthwave Echo",
            album = "Retro Future",
            folderName = "Synthwave",
            isFavorite = false,
        ),
        MusicModel(
            musicId = 103,
            uri = "content://media/external/audio/media/103",
            path = "/storage/emulated/0/Music/midnight_coffee.mp3",
            mimeTypes = "audio/x-flac",
            name = "Midnight Coffee",
            duration = 150000,
            size = 15728640,
            artworkUri = "",
            bitrate = 1411,
            artist = "The Lo-Fi Crew",
            album = "Chill Beats Vol. 1",
            folderName = "Lo-Fi Study",
            isFavorite = false,
        ),
    )
    val mockUiState = CategoryUiState(
        detail = "4 Songs • 13 mins",
        songList = dummyMusicList,
        playingMusicState = PlayingMusicState(
            isPlaying = true,
            playingMusicInfo = PlayingMusicInfo(
                title = "Blinding Lights",
                musicID = "101",
                artworkUri = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=250",
                musicUri = "content://media/external/audio/media/101",
                artist = "The Weeknd",
                album = "After Hours",
                duration = 200000L,
                bitrate = 320,
                size = 8388608L,
            ),
            playerRepeatMode = PlayerRepeatMode.MODE_OFF,
            isFavorite = false,
            isBuffering = true,
            isShuffleMode = false,
        ),
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                CategoryDetailComponent(
                    modifier = Modifier.fillMaxSize(),
                    categoryUiState = mockUiState,
                    categoryName = "Synthwave Classics",
                    onMusicClick = {},
                    animatedVisibilityScope = this,
                    showArtworkOnTop = true,
                )
            }
        }
    }
}
