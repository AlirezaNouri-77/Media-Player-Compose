package com.example.feature.music_categorydetail.component

import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
internal fun SharedTransitionScope.CategoryDetailPortrait(
    modifier: Modifier = Modifier,
    uiState: CategoryUiState,
    categoryName: String,
    currentMusicId: String,
    isPlayerPlaying: Boolean,
    onMusicClick: (index: Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
) {
    var currentImageSize by rememberSaveable {
        mutableStateOf(240.dp)
    }

    val thumbNailNestedConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val delta = available.y.toInt()
                val newSize = currentImageSize.value + delta
                val pre = currentImageSize.value
                currentImageSize = newSize.coerceIn(160f, 240f).dp
                return Offset(x = 0f, y = currentImageSize.value - pre)
            }
        }
    }

    if (uiState.songList.isNotEmpty()) {
        Column(
            modifier = modifier.then(
                if (displayWithVisuals) {
                    Modifier.nestedScroll(thumbNailNestedConnection)
                } else {
                    Modifier
                },
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (displayWithVisuals) {
                MusicThumbnail(
                    uri = uiState.songList.firstOrNull { it.artworkUri.isNotEmpty() }?.artworkUri?.toUri()
                        ?: Uri.EMPTY,
                    modifier = Modifier
                        .size(currentImageSize)
                        .clip(RoundedCornerShape(15.dp)),
                )
            }
            Text(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        renderInOverlayDuringTransition = false,
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
                        musicId = item.musicId,
                        artworkUri = item.artworkUri,
                        name = item.name,
                        artist = item.artist,
                        duration = item.duration,
                        isFavorite = item.isFavorite,
                        currentMediaId = currentMusicId,
                        onItemClick = { onMusicClick(index) },
                        isPlaying = { isPlayerPlaying },
                    )
                }
            }
        }
    }
}
