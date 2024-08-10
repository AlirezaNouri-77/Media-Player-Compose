package com.example.mediaplayerjetpackcompose.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerjetpackcompose.domain.model.navigation.BottomBarNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.bottombar.BottomNavigationBar
import com.example.mediaplayerjetpackcompose.presentation.screen.component.navigation.BottomBarNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.FullMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RootScreen(
  musicPageViewModel: MusicPageViewModel = koinViewModel(),
  videoPageViewModel: VideoPageViewModel = koinViewModel(),
) {


  val navHostController: NavHostController = rememberNavController()
  val navBackStackEntry by navHostController.currentBackStackEntryAsState()
  val currentRoute = remember(navBackStackEntry) {
    navBackStackEntry?.destination?.route
  }
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value

  val pagerState = rememberPagerState(
    initialPage = musicPageViewModel.currentPagerPage.intValue,
    pageCount = { musicPageViewModel.pagerItemList.size },
  )

  LaunchedEffect(key1 = currentMusicState.mediaId) {
    val index = musicPageViewModel.pagerItemList.indexOfFirst { it.musicId == currentMusicState.mediaId.toLong() }
    musicPageViewModel.currentPagerPage.intValue = index
    pagerState.animateScrollToPage(index, animationSpec = tween(durationMillis = 400))
  }

  LaunchedEffect(key1 = musicPageViewModel.currentPagerPage.intValue) {
    if (pagerState.currentPage == musicPageViewModel.currentPagerPage.intValue) return@LaunchedEffect
    pagerState.animateScrollToPage(
      musicPageViewModel.currentPagerPage.intValue,
      animationSpec = tween(durationMillis = 400)
    )
  }

  LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
    if (pagerState.currentPage == musicPageViewModel.currentPagerPage.intValue) return@LaunchedEffect
    if (pagerState.currentPage != musicPageViewModel.currentPagerPage.intValue && !pagerState.isScrollInProgress) {
      if (pagerState.currentPage > musicPageViewModel.currentPagerPage.intValue) {
        musicPageViewModel.moveToNext()
      } else if (pagerState.currentPage < musicPageViewModel.currentPagerPage.intValue) {
        musicPageViewModel.moveToPrevious(true)
      }
      musicPageViewModel.currentPagerPage.intValue = pagerState.currentPage
    }
  }

  LaunchedEffect(key1 = currentMusicState.metaData) {
    musicPageViewModel.getColor(currentMusicState.uri)
  }

  Scaffold(
    bottomBar = {
      AnimatedVisibility(
        visible = currentRoute != BottomBarNavigationModel.VideoPlayerScreen.route,
        enter = slideInVertically(
          tween(durationMillis = 300),
          initialOffsetY = { int -> int / 2 },
        ),
        exit = slideOutVertically(
          tween(durationMillis = 300),
          targetOffsetY = { int -> int / 2 },
        ),
      ) {
        BottomNavigationBar(
          currentRoute = { currentRoute },
          onClick = navHostController::navigate,
        )
      }
    },
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
      BottomBarNavController(
        navHostController = navHostController,
        musicPageViewModel = musicPageViewModel,
        videoPageViewModel = videoPageViewModel,
        pagerState = pagerState,
      )
    }
  }

  AnimatedVisibility(
    visible = musicPageViewModel.isFullPlayerShow,
    enter = fadeIn(tween(300, 100)) + slideInVertically(
      animationSpec = tween(300),
      initialOffsetY = { int -> int / 4 }),
    exit = slideOutVertically(
      animationSpec = tween(300, 100),
      targetOffsetY = { int -> int / 4 }) + fadeOut(tween(300, 100))
  ) {

    FullMusicPlayer(
      currentMediaCurrentState = { currentMusicState },
      favoriteList = musicPageViewModel.favoriteListMediaId,
      pagerMusicList = musicPageViewModel.pagerItemList,
      backgroundColorByArtwork = musicPageViewModel.musicArtworkColorPalette,
      repeatMode = musicPageViewModel.currentRepeatMode.intValue,
      pagerState = pagerState,
      onPauseMusic = musicPageViewModel::pauseMusic,
      onResumeMusic = musicPageViewModel::resumeMusic,
      onMoveNextMusic = musicPageViewModel::moveToNext,
      onMovePreviousMusic = musicPageViewModel::moveToPrevious,
      onSeekTo = musicPageViewModel::seekToPosition,
      onRepeatMode = musicPageViewModel::setRepeatMode,
      onBack = { musicPageViewModel.isFullPlayerShow = false },
      currentMusicPosition = { musicPageViewModel.currentMusicPosition.floatValue.toLong() },
      onFavoriteToggle = { musicPageViewModel.handleFavoriteSongs(currentMusicState.mediaId) },
    )

  }

}

