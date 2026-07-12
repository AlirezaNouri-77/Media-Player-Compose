package com.shermanrex.core.designsystem.music

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shermanrex.core.designsystem.CategoryListItem
import com.shermanrex.core.designsystem.EmptyPage
import com.shermanrex.core.designsystem.util.listBottomContentSpace
import com.shermanrex.core.model.MusicModel

@Composable
fun CategoryListComponent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    listItem: List<Pair<String, List<MusicModel>>>,
    currentMusicId: String,
    sharedTransitionScope: SharedTransitionScope,
    navigateToCategoryPage: (String) -> Unit,
) {
    if (listItem.isNotEmpty()) {
        LazyColumn(
            state = lazyListState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = listBottomContentSpace(currentMusicId.isNotEmpty()),
            ),
        ) {
            items(
                items = listItem,
                key = { it },
            ) { item ->
                CategoryListItem(
                    categoryName = item.first,
                    musicListSize = item.second.size,
                    thumbnailUri = item.second.first { it.artworkUri.isNotEmpty() }.artworkUri,
                    onClick = { categoryName ->
                        navigateToCategoryPage(categoryName)
                    },
                    sharedTransitionScope = sharedTransitionScope,
                )
            }
        }
    } else {
        EmptyPage()
    }
}
