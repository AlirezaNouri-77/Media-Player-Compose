package com.example.core.designsystem.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun rememberLazyListState(
    initialIndex: Int = 0,
    onStopScroll: (Int) -> Unit,
): LazyListState {
    val lazyList = rememberLazyListState(initialIndex)

    LaunchedEffect(initialIndex) {
        if (initialIndex != 0) lazyList.scrollToItem(initialIndex)
    }

    LaunchedEffect(lazyList) {
        snapshotFlow {
            lazyList.firstVisibleItemIndex
        }.debounce(500)
            .collectLatest {
                if (!lazyList.isScrollInProgress) onStopScroll(it)
            }
    }

    return lazyList
}
