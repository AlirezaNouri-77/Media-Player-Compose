package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shermanrex.core.designsystem.music.MusicThumbnail
import com.shermanrex.core.music_media3.model.ArtworkModel
import com.shermanrex.feature.music_player.PagerHandler
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FullscreenPlayerPager(
    modifier: Modifier = Modifier,
    pagerItem: ImmutableList<ArtworkModel>,
    currentMusicID: Long,
    onMoveToIndexPager: (Int, String) -> Unit,
    setCurrentPagerIndex: (Int) -> Unit,
    currentPagerPage: Int,
) {
    val pagerState = rememberPagerState(
        initialPage = currentPagerPage,
        pageCount = { pagerItem.size },
    )

    PagerHandler(
        currentPlayerMediaId = currentMusicID,
        pagerMusicList = pagerItem,
        currentPagerPage = currentPagerPage,
        pagerState = pagerState,
        setCurrentPagerNumber = setCurrentPagerIndex,
        onMoveToIndex = onMoveToIndexPager,
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
            beyondViewportPageCount = 4,
            state = pagerState,
            pageSpacing = 24.dp,
            verticalAlignment = Alignment.CenterVertically,
            key = { index -> if (index in pagerItem.indices) pagerItem[index].musicId else index },
        ) { page ->
            MusicThumbnail(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
                uri = pagerItem[page].uri.toString(),
            )
        }
    }
}
