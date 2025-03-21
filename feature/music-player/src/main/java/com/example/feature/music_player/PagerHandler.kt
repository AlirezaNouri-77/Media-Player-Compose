package com.example.feature.music_player

import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PagerHandler(
  currentPlayerMediaId: Long,
  pagerMusicList: ImmutableList<MusicModel>,
  currentPagerPage: Int,
  pagerState: PagerState,
  setCurrentPagerNumber: (Int) -> Unit,
  onMoveToIndex: (Int) -> Unit,
) {
  // when current music track is end and moved to the next music it cause mediaId change and this this execute
  LaunchedEffect(key1 = currentPlayerMediaId) {
    val index = pagerMusicList.indexOfFirst { it.musicId == currentPlayerMediaId }
    if (currentPagerPage == index && pagerState.isScrollInProgress) return@LaunchedEffect
    setCurrentPagerNumber(index)
    pagerState.animateScrollToPage(index, animationSpec = tween(durationMillis = 300))
  }

  LaunchedEffect(key1 = currentPagerPage) {
    if (pagerState.settledPage == currentPagerPage && pagerState.isScrollInProgress) return@LaunchedEffect
    pagerState.animateScrollToPage(
      currentPagerPage ,
      animationSpec = tween(durationMillis = 300)
    )
  }

  LaunchedEffect(key1 = pagerState.settledPage, key2 = pagerState.isScrollInProgress) {
    if (pagerState.settledPage == currentPagerPage ) return@LaunchedEffect
    if (pagerState.settledPage != currentPagerPage  && !pagerState.isScrollInProgress) {
      onMoveToIndex(pagerState.settledPage)
      setCurrentPagerNumber(pagerState.settledPage)
    }
  }
}