package com.example.feature.music_categorydetail.component

import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.MusicThumbnail
import com.example.core.designsystem.util.MiniPlayerHeight
import com.example.core.designsystem.util.NavigationBottomBarHeight
import com.example.feature.music_categorydetail.CategoryUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun SharedTransitionScope.CategoryDetail(
    modifier: Modifier = Modifier,
    uiState: CategoryUiState,
    categoryName: String,
    currentMusicId: String,
    isPlayerPlaying: Boolean,
    onMusicClick: (index: Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "",
                        modifier = Modifier.size(35.dp),
                    )
                }
                if (displayWithVisuals) {
                    MusicThumbnail(
                        uri = uiState.songList.firstOrNull { it.artworkUri.isNotEmpty() }?.artworkUri?.toUri()
                            ?: Uri.EMPTY,
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("thumbnailKey$categoryName"),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                    )
                }
            }
            Text(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp)
                    .basicMarquee(),
                text = categoryName,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = uiState.detail,
                fontSize = 16.sp,
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(
                top = 10.dp,
                bottom = NavigationBottomBarHeight + MiniPlayerHeight,
            ),
        ) {
            itemsIndexed(
                items = uiState.songList,
                key = { _, item -> item.musicId },
            ) { index, item ->
                MusicMediaItem(
                    currentMediaId = currentMusicId,
                    onItemClick = {
                        onMusicClick.invoke(index)
                    },
                    isPlaying = { isPlayerPlaying },
                    musicId = item.musicId,
                    artworkUri = item.artworkUri,
                    name = item.name,
                    artist = item.artist,
                    duration = item.duration,
                    isFavorite = item.isFavorite,
                )
            }
        }
    }
}
