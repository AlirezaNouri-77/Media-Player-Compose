package com.shermanrex.core.designsystem.music

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shermanrex.core.designsystem.EmptyPage
import com.shermanrex.core.designsystem.util.listBottomContentSpace
import com.shermanrex.core.model.MusicModel

@Composable
fun MusicListComponent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    listItem: List<MusicModel>,
    isPlaying: Boolean,
    currentMusicId: String,
    onMusicClick: (Int, List<MusicModel>) -> Unit,
) {
    if (listItem.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(
                bottom = listBottomContentSpace(currentMusicId.isNotEmpty()),
            ),
        ) {
            itemsIndexed(
                items = listItem,
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
                    onItemClick = { onMusicClick(index, listItem) },
                    isPlaying = { isPlaying },
                )
            }
        }
    } else {
        EmptyPage()
    }
}
