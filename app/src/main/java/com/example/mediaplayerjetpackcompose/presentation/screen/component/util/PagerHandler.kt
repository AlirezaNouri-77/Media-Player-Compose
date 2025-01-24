package com.example.mediaplayerjetpackcompose.presentation.screen.component.util

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.PagerThumbnailModel

@Composable
fun PagerHandler(
  currentPlayerMediaId: Long,
  pagerMusicList: () -> List<MusicModel>,
  currentPagerPage: () -> Int,
  pagerState: PagerState,
  setCurrentPagerNumber: (Int) -> Unit,
  onMoveToIndex: (Int) -> Unit,
) {
  // when current music track is end and moved to the next music it cause mediaId change and this this execute
  LaunchedEffect(key1 = currentPlayerMediaId) {
    val index = pagerMusicList().indexOfFirst { it.musicId == currentPlayerMediaId }
    if (currentPagerPage() == index && pagerState.isScrollInProgress) return@LaunchedEffect
    setCurrentPagerNumber(index)
    pagerState.animateScrollToPage(index, animationSpec = tween(durationMillis = 300))
  }

  LaunchedEffect(key1 = currentPagerPage()) {
    if (pagerState.settledPage == currentPagerPage() && pagerState.isScrollInProgress) return@LaunchedEffect
    pagerState.animateScrollToPage(
      currentPagerPage() ,
      animationSpec = tween(durationMillis = 300)
    )
  }

  LaunchedEffect(key1 = pagerState.settledPage, key2 = pagerState.isScrollInProgress) {
    if (pagerState.settledPage == currentPagerPage() ) return@LaunchedEffect
    if (pagerState.settledPage != currentPagerPage()  && !pagerState.isScrollInProgress) {
      onMoveToIndex(pagerState.settledPage)
      setCurrentPagerNumber(pagerState.settledPage)
    }
  }
}