package com.example.mediaplayerjetpackcompose.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.component.navigation.ScreenNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.FullMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootScreen(
  musicPageViewModel: MusicPageViewModel = koinViewModel(),
  videoPageViewModel: VideoPageViewModel = koinViewModel(),
) {

  val navHostController: NavHostController = rememberNavController()
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value

  LaunchedEffect(key1 = currentMusicState.metaData) {
    musicPageViewModel.getColorPaletteFromArtwork(currentMusicState.uri)
  }

  ScreenNavController(
    navHostController = navHostController,
    musicPageViewModel = musicPageViewModel,
    videoPageViewModel = videoPageViewModel,
  )

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
      currentMediaState = { currentMusicState },
      favoriteList = musicPageViewModel.favoriteListMediaId,
      pagerMusicList = musicPageViewModel.pagerItemList,
      backgroundColorByArtwork = musicPageViewModel.musicArtworkColorPalette,
      repeatMode = musicPageViewModel.currentRepeatMode.intValue,
      currentPagerPage = musicPageViewModel.currentPagerPage.intValue,
      setCurrentPagerNumber = { musicPageViewModel.currentPagerPage.intValue = it },
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

