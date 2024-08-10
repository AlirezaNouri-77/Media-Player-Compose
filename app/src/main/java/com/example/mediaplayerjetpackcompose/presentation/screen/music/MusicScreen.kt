package com.example.mediaplayerjetpackcompose.presentation.screen.music

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.presentation.screen.component.topbar.TopBarMusic
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.CategoryPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MiniMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MusicList
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  pagerState: PagerState,
  density: Density = LocalDensity.current,
) {

  var showSortBar by remember { mutableStateOf(false) }
  var showSearch by remember { mutableStateOf(false) }

  val navController: NavHostController = rememberNavController()
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value

  val bottomPadding = remember {
    mutableStateOf(0.dp)
  }

  LaunchedEffect(key1 = showSortBar) {
    delay(15000L)
    showSortBar = false
  }

  ConstraintLayout(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    val (musicList, collapsePlayer) = createRefs()

    Box(
      modifier = Modifier.constrainAs(musicList) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom)
      },
    ) {
      NavHost(
        navController = navController,
        startDestination = "Home",
      ) {
        composable("Home") {
          Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
              TopBarMusic(
                musicPageViewModel = musicPageViewModel,
                showSortBar = showSortBar,
                showSearch = showSearch,
                currentTabPosition = musicPageViewModel.currentTabState,
                onSortIconClick = { showSortBar = !showSortBar },
                onSearchIconClick = { showSearch = !showSearch },
              )
            },
          ) { paddingValue ->

            if (musicPageViewModel.isLoading) {
              Box(
                Modifier
                  .padding(paddingValue)
                  .fillMaxSize(),
                contentAlignment = Alignment.Center
              ) {
                Text(text = "Loading")
              }
            } else {
              MusicList(
                musicPageViewModel = musicPageViewModel,
                currentMusicState = currentMusicState,
                navController = navController,
                paddingValue = paddingValue,
                bottomPadding = bottomPadding.value,
              )
            }
          }
        }

        composable(
          "Category/{CategoryName}",
          arguments = listOf(
            navArgument(name = "CategoryName") {
              type = NavType.StringType
            },
          )
        ) {
          CategoryPage(
            name = it.arguments!!.getString("CategoryName").toString(),
            currentMediaCurrentState = currentMusicState,
            musicPageViewModel = musicPageViewModel,
            onMusicClick = { index, musicList ->
              musicPageViewModel.playMusic(
                index = index,
                musicList
              )
            },
            onBackClick = navController::popBackStack
          )
        }

      }
    }


    AnimatedVisibility(
      visible = currentMusicState.mediaId.isNotEmpty(),
      modifier = Modifier
        .constrainAs(collapsePlayer) {
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          bottom.linkTo(parent.bottom)
        },
      enter = fadeIn(tween(100)) + slideInVertically(
        animationSpec = tween(400),
        initialOffsetY = { int -> int / 4 }),
      exit = slideOutVertically(
        animationSpec = tween(400, 100),
        targetOffsetY = { int -> int / 4 }) + fadeOut(tween(100))
    ) {
      MiniMusicPlayer(
        modifier = Modifier.onGloballyPositioned { bottomPadding.value = density.run { it.size.height.toDp() } },
        pagerState = pagerState,
        pagerMusicList = musicPageViewModel.pagerItemList,
        artworkColorPalette = musicPageViewModel.musicArtworkColorPalette,
        onPauseMusic = musicPageViewModel::pauseMusic,
        onResumeMusic = musicPageViewModel::resumeMusic,
        currentMediaCurrentState = { currentMusicState },
        currentMusicPosition = { musicPageViewModel.currentMusicPosition.floatValue.toLong() },
        onClick = { musicPageViewModel.isFullPlayerShow = true },
      )
    }

  }
}