package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.Loading
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.CategoryPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MiniMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MusicListHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.FullMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.TabBarSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.TopBarMusic
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  onNavigateToVideoScreen: () -> Unit,
  density: Density = LocalDensity.current,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  var showSortBar by remember { mutableStateOf(false) }
  val navController: NavHostController = rememberNavController()

  val currentMusicState by musicPageViewModel.currentMusicState.collectAsStateWithLifecycle()
  val favoriteMusicMediaIdList by musicPageViewModel.favoriteMusic.collectAsStateWithLifecycle()
  val currentMusicPosition by musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle()
  val currentDeviceVolume by musicPageViewModel.currentDeviceVolume.collectAsStateWithLifecycle()
  val sortState by musicPageViewModel.sortState.collectAsStateWithLifecycle()

  var isKeyboardFocus by remember { mutableStateOf(false) }

  var miniPlayerHeight by remember { mutableStateOf(0.dp) }

  var showSearch by remember { mutableStateOf(false) }

  LaunchedEffect(key1 = currentMusicState.metaData) {
    musicPageViewModel.getColorPaletteFromArtwork(currentMusicState.uri)
  }

  BackHandler {
    if (musicPageViewModel.isFullPlayerShow) {
      musicPageViewModel.isFullPlayerShow = false
    }
  }

  ConstraintLayout(
    modifier = Modifier
      .fillMaxSize()
      .then(
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.displayCutoutPadding() else Modifier
      ),
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
          modifier = Modifier.imePadding(),
          topBar = {
            TopBarMusic(
              currentTabPosition = musicPageViewModel.currentTabState,
              onVideoIconClick = { onNavigateToVideoScreen() },
              onSearch = { musicPageViewModel.searchMusic(it) },
              onSortIconClick = { showSortBar = true },
              isDropDownMenuSortExpand = showSortBar,
              sortState = { sortState },
              onDismiss = { showSortBar = false },
              onSortClick = {
                musicPageViewModel.updateSortType(it)
                musicPageViewModel.sortMusicListByCategory(list = musicPageViewModel.musicList)
              },
              onOrderClick = {
                musicPageViewModel.updateSortIsDec(!sortState.isDec)
                musicPageViewModel.sortMusicListByCategory(list = musicPageViewModel.musicList)
              },
              onKeyboardFocusChange = {
                isKeyboardFocus = it
              },
              isSearchShow = { showSearch },
              onSearchClick = { showSearch = it },
            )
          },
        ) { paddingValue ->

          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValue),
          ) {

            AnimatedVisibility(
              visible = !showSearch,
              enter = fadeIn(tween(50)) + slideInVertically(tween(100, 50)),
              exit = slideOutVertically(tween(400)) { -it } + fadeOut(tween(50, 400))
            ) {
              TabBarSection(
                currentTabState = musicPageViewModel.currentTabState,
                onTabClick = { tabBar, _ ->
                  musicPageViewModel.currentTabState = tabBar
                }
              )
            }

            Crossfade(
              modifier = Modifier
                .fillMaxSize(),
              targetState = showSearch,
              label = "",
            ) {
              if (it == false) {
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
                      favoriteMusicMediaIdList = favoriteMusicMediaIdList.map { it.mediaId },
                      bottomPadding = miniPlayerHeight,
                      navigateTo = { navController.navigate(it) },
                    )
                  }
                }
              } else {

                LazyColumn(
                  modifier = Modifier
                    .fillMaxSize(),
                  contentPadding = PaddingValues(bottom = if (isKeyboardFocus) 0.dp else miniPlayerHeight, top = 10.dp),
                ) {
                  itemsIndexed(
                    items = musicPageViewModel.searchList,
                    key = { _, item -> item.musicId },
                  ) { index, item ->
                    MusicMediaItem(
                      item = item,
                      isFav = item.musicId.toString() in favoriteMusicMediaIdList.map { it.mediaId },
                      currentMediaId = currentMusicState.mediaId,
                      onItemClick = {
                        musicPageViewModel.playMusic(
                          index = index,
                          musicPageViewModel.searchList
                        )
                      },
                      isPlaying = currentMusicState.isPlaying,
                    )
                  }
                }
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
        favoriteList = favoriteMusicMediaIdList.map { it.mediaId },
        pagerMusicList = musicPageViewModel.pagerItemList,
        backgroundColorByArtwork = musicPageViewModel.musicArtworkColorPalette,
        repeatMode = currentMusicState.repeatMode,
        currentPagerPage = musicPageViewModel.currentPagerPage.intValue,
        onPlayerAction = musicPageViewModel::onPlayerAction,
        currentVolume = currentDeviceVolume,
        currentMediaState = { currentMusicState },
        setCurrentPagerNumber = { musicPageViewModel.currentPagerPage.intValue = it },
        onBack = { musicPageViewModel.isFullPlayerShow = false },
        currentMusicPosition = { currentMusicPosition },
        onVolumeChange = { musicPageViewModel.setDeviceVolume(it) },
        maxDeviceVolume = musicPageViewModel.getMaxDeviceVolume(),
      )
    }

  }
}