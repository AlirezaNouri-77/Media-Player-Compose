package com.example.mediaplayerjetpackcompose.presentation.screen.music

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.Loading
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.CategoryPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MiniMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MusicListHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.FullMusicPlayer
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
  val favoriteMusicMediaIdList = musicPageViewModel.favoriteMusic.collectAsStateWithLifecycle().value.map { it.mediaId }
  val currentMusicPosition = musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle().value

  var miniPlayerHeight by remember { mutableStateOf(0.dp) }

  LaunchedEffect(key1 = currentMusicState.metaData) {
    musicPageViewModel.getColorPaletteFromArtwork(currentMusicState.uri)
  }

  BackHandler {
    if (musicPageViewModel.isFullPlayerShow) {
      musicPageViewModel.isFullPlayerShow = false
    }
  }

  SortDropDownMenu(
    isExpand = showSortBar,
    sortState = musicPageViewModel.sortState,
    offset = sortIconOffset,
    onDismiss = { showSortBar = false },
    onSortClick = {
      musicPageViewModel.sortState = musicPageViewModel.sortState.copy(sortType = it)
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
      .fillMaxSize()
  ) {
    val (musicList, collapsePlayer) = createRefs()

    NavHost(
      modifier = Modifier
        .constrainAs(musicList) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          bottom.linkTo(parent.bottom)
        },
      navController = navController,
      startDestination = MusicNavigationModel.Home,
    ) {
      composable<MusicNavigationModel.Home> {
        Scaffold(
          topBar = {
            TopBarMusic(
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
                MusicListHandler(
                  musicPageViewModel = musicPageViewModel,
                  currentMusicState = currentMusicState,
                  navController = navController,
                  bottomPadding = miniPlayerHeight,
                  favoriteMusicMediaIdList = favoriteMusicMediaIdList,
                )
              }
            }
          }

        }

      }

      composable<MusicNavigationModel.Category> { backStackEntry ->
        val categoryName = backStackEntry.toRoute<MusicNavigationModel.Category>().name

        val itemList = remember(categoryName) {
          when (musicPageViewModel.currentTabState) {
            TabBarPosition.ARTIST -> musicPageViewModel.artistsMusicMap.first { it.categoryName == categoryName }.categoryList
            TabBarPosition.ALBUM -> musicPageViewModel.albumMusicMap.first { it.categoryName == categoryName }.categoryList
            TabBarPosition.Folder -> musicPageViewModel.folderMusicMap.first { it.categoryName == categoryName }.categoryList
            else -> emptyList()
          }
        }

        CategoryPage(
          name = categoryName,
          itemList = itemList,
          currentCurrentMediaState = currentMusicState,
          onMusicClick = { index ->
            musicPageViewModel.playMusic(
              index = index,
              musicList = itemList,
            )
          },
          miniPlayerHeight = miniPlayerHeight,
          onBackClick = navController::popBackStack
        )
      }

    }

    AnimatedVisibility(
      visible = currentMusicState.mediaId.isNotEmpty() && !musicPageViewModel.isFullPlayerShow,
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
        onClick = { musicPageViewModel.isFullPlayerShow = true },
        modifier = Modifier.onGloballyPositioned { miniPlayerHeight = density.run { it.size.height.toDp() } },
        pagerMusicList = musicPageViewModel.pagerItemList,
        currentMediaState = { currentMusicState },
        currentMusicPosition = { currentMusicPosition },
        currentPagerPage = musicPageViewModel.currentPagerPage.intValue,
        setCurrentPagerNumber = { musicPageViewModel.currentPagerPage.intValue = it },
        onPlayerAction = musicPageViewModel::onPlayerAction,
      )
    }

    AnimatedVisibility(
      visible = musicPageViewModel.isFullPlayerShow,
      enter = fadeIn(tween(300, 100)) + slideInVertically(
        animationSpec = tween(350),
        initialOffsetY = { int -> int / 4 }),
      exit = slideOutVertically(
        animationSpec = tween(300, 100),
        targetOffsetY = { int -> int / 4 }) + fadeOut(tween(350, 50))
    ) {
      FullMusicPlayer(
        currentMediaState = { currentMusicState },
        favoriteList = favoriteMusicMediaIdList,
        pagerMusicList = musicPageViewModel.pagerItemList,
        backgroundColorByArtwork = musicPageViewModel.musicArtworkColorPalette,
        repeatMode = currentMusicState.repeatMode,
        currentPagerPage = musicPageViewModel.currentPagerPage.intValue,
        setCurrentPagerNumber = { musicPageViewModel.currentPagerPage.intValue = it },
        onBack = { musicPageViewModel.isFullPlayerShow = false },
        currentMusicPosition = { currentMusicPosition },
        onPlayerAction = musicPageViewModel::onPlayerAction,
      )
    }

  }
}