package com.example.mediaplayerjetpackcompose.presentation.screen.music

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.Loading
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.CategoryPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MiniMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MusicList
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.SortDropDownMenu
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.TabBarSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.TopBarMusic

@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  onNavigateToVideoScreen: () -> Unit,
  density: Density = LocalDensity.current,
) {

  var showSortBar by remember { mutableStateOf(false) }
  var sortIconOffset by remember { mutableStateOf(DpOffset.Zero) }
  val navController: NavHostController = rememberNavController()
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value

  val miniPlayerHeight = remember {
    mutableStateOf(0.dp)
  }

  SortDropDownMenu(
    isExpand = showSortBar,
    sortState = musicPageViewModel.sortState,
    offset = sortIconOffset,
    onDismiss = { showSortBar = false },
    onSortClick = {
      musicPageViewModel.sortState =  musicPageViewModel.sortState.copy(sortType = it)
      musicPageViewModel.sortMusicListByCategory(
        list = musicPageViewModel.musicList
      ).also { resultList ->
        musicPageViewModel.musicList =
          resultList as SnapshotStateList<MusicModel>
      }
    },
    onOrderClick = {
      musicPageViewModel.sortState =
        musicPageViewModel.sortState.copy(isDec = !musicPageViewModel.sortState.isDec)
      musicPageViewModel.sortMusicListByCategory(
        list = musicPageViewModel.musicList
      ).also { resultList ->
        musicPageViewModel.musicList = resultList as SnapshotStateList<MusicModel>
      }
    },
  )

  ConstraintLayout(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    val (musicList, collapsePlayer, tabBar) = createRefs()

    NavHost(
      modifier = Modifier.constrainAs(musicList) {
        top.linkTo(tabBar.bottom)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom)
      },
      navController = navController,
      startDestination = "Home",
    ) {
      composable("Home") {
        Scaffold(
          topBar = {
            TopBarMusic(
              density = density,
              currentTabPosition = musicPageViewModel.currentTabState,
              onVideoIconClick = { onNavigateToVideoScreen() },
              onSearch = { musicPageViewModel.searchMusic(it) },
              onSortIconClick = { showSortBar = true },
              sortIconOffset = { sortIconOffset = it },
            )
          },
        ) { paddingValue ->

          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValue),
          ) {
            TabBarSection(
              currentTabState = musicPageViewModel.currentTabState,
              onTabClick = { tabBar, _ ->
                musicPageViewModel.currentTabState = tabBar
              }
            )
            Crossfade(
              modifier = Modifier
                .fillMaxSize(),
              targetState = musicPageViewModel.isLoading,
              label = "",
            ) { target ->
              if (target) {
                Loading(modifier = Modifier)
              } else {
                MusicList(
                  musicPageViewModel = musicPageViewModel,
                  currentMusicState = currentMusicState,
                  navController = navController,
                  bottomPadding = miniPlayerHeight.value,
                )
              }
            }
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

    AnimatedVisibility(
      visible = currentMusicState.mediaId.isNotEmpty(),
      modifier = Modifier.constrainAs(collapsePlayer) {
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom)
      },
      enter = fadeIn(tween(200, delayMillis = 90)) + slideInVertically(
        animationSpec = tween(400, 100),
        initialOffsetY = { int -> int / 4 }),
      exit = slideOutVertically(
        animationSpec = tween(400, 100),
        targetOffsetY = { int -> int / 4 }) + fadeOut(tween(200, delayMillis = 90))
    ) {
      MiniMusicPlayer(
        modifier = Modifier.onGloballyPositioned { miniPlayerHeight.value = density.run { it.size.height.toDp() } },
        pagerMusicList = musicPageViewModel.pagerItemList,
        onPauseMusic = musicPageViewModel::pauseMusic,
        onResumeMusic = musicPageViewModel::resumeMusic,
        currentMediaState = { currentMusicState },
        currentMusicPosition = { musicPageViewModel.currentMusicPosition.floatValue.toLong() },
        onClick = { musicPageViewModel.isFullPlayerShow = true },
        onMovePreviousMusic = { musicPageViewModel.moveToPrevious(it) },
        onMoveNextMusic = musicPageViewModel::moveToNext,
        currentPagerPage = musicPageViewModel.currentPagerPage.intValue,
        setCurrentPagerNumber = { musicPageViewModel.currentPagerPage.intValue = it }
      )
    }

  }
}