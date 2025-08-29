package com.example.feature.music_player.miniPlayer.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.common.util.removeFileExtension
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MiniPlayerPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    artworkList: ImmutableList<MusicModel>,
) {
    val marqueeAnimate = remember(pagerState.isScrollInProgress) {
        if (pagerState.isScrollInProgress) 0 else Int.MAX_VALUE
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            pageSpacing = 20.dp,
            beyondViewportPageCount = 2,
            contentPadding = PaddingValues(horizontal = 2.dp),
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.95f),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(
                            iterations = marqueeAnimate,
                            initialDelayMillis = 500,
                        ),
                    text = artworkList[page].name.removeFileExtension(),
                    fontSize = 13.sp,
                    maxLines = 1,
                    color = Color.White,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = artworkList[page].artist,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = Color.White,
                )
            }
        }
    }
}
