package com.example.mediaplayerjetpackcompose.presentation.screen.component.util

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerHandler(
  currentMediaState: () -> CurrentMediaState,
  pagerMusicList: List<MusicModel>,
  currentPagerPage: Int,
  pagerState: PagerState,
  setCurrentPagerNumber: (Int) -> Unit,
  onMoveNextMusic: () -> Unit,
  onMovePreviousMusic: (Boolean) -> Unit
) {
  LaunchedEffect(key1 = currentMediaState().mediaId) {
    val index = pagerMusicList.indexOfFirst { it.musicId == currentMediaState().mediaId.toLong() }
    if (currentPagerPage == index && pagerState.isScrollInProgress) return@LaunchedEffect
    setCurrentPagerNumber(index)
    pagerState.animateScrollToPage(index, animationSpec = tween(durationMillis = 300))
  }

  LaunchedEffect(key1 = currentPagerPage) {
    if (pagerState.settledPage == currentPagerPage && pagerState.isScrollInProgress) return@LaunchedEffect
    pagerState.animateScrollToPage(
      currentPagerPage,
      animationSpec = tween(durationMillis = 300)
    )
  }

  LaunchedEffect(key1 = pagerState.settledPage, key2 = pagerState.isScrollInProgress) {
    if (pagerState.settledPage == currentPagerPage) return@LaunchedEffect
    if (pagerState.settledPage != currentPagerPage && !pagerState.isScrollInProgress) {
      if (pagerState.settledPage > currentPagerPage) {
        onMoveNextMusic()
      } else if (pagerState.settledPage < currentPagerPage) {
        onMovePreviousMusic(true)
      }
      setCurrentPagerNumber(pagerState.settledPage)
    }
  }
}