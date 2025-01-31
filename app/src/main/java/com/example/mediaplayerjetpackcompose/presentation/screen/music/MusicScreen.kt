package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.data.util.Constant.MINI_PLAYER_HEIGHT
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.navigation.ParentRoute
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.AlbumPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.ArtistPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.CategoryPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.HomeMusic
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MiniMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.SearchPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.bottomBar.MusicNavigationBar
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.FullMusicPlayer
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  onNavigateToVideoScreen: () -> Unit,
  density: Density = LocalDensity.current,
  orientation: Int = LocalConfiguration.current.orientation,
  screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp,
) {

  val miniPlayerHeight = MINI_PLAYER_HEIGHT

  val navController: NavHostController = rememberNavController()

  var bottomSheetState = rememberStandardBottomSheetState()
  var bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

  var coroutineScope = rememberCoroutineScope()

  val currentMusicState by musicPageViewModel.currentMusicState.collectAsStateWithLifecycle()

  val currentMusicPosition by musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle()
  val currentDeviceVolume by musicPageViewModel.currentDeviceVolume.collectAsStateWithLifecycle()
  val sortState by musicPageViewModel.sortState.collectAsStateWithLifecycle()
  val currentArtworkPagerIndex by musicPageViewModel.currentArtworkPagerIndex.collectAsStateWithLifecycle()
  val artworkPagerList by musicPageViewModel.artworkPagerList.collectAsStateWithLifecycle()
  val favoriteSongsList by musicPageViewModel.favoriteSongsList.collectAsStateWithLifecycle()

  val favoriteSongsMediaId by musicPageViewModel.favoriteSongsMediaId.collectAsStateWithLifecycle()

  val album by musicPageViewModel.album.collectAsStateWithLifecycle()
  val folder by musicPageViewModel.folder.collectAsStateWithLifecycle()
  val artist by musicPageViewModel.artist.collectAsStateWithLifecycle()

  var bottomBarHeight by remember { mutableStateOf(0.dp) }

  var screenHeightPx = remember(screenHeight) {
    with(density) {
      screenHeight.toPx()
    }
  }

  val musicArtWorkColorAnimation by animateColorAsState(
    targetValue = Color(musicPageViewModel.musicArtworkColorPalette),
    animationSpec = tween(durationMillis = 180, delayMillis = 120),
    label = "",
  )

  LaunchedEffect(key1 = currentMusicState.metaData) {
    if (currentMusicState.mediaId.isEmpty()) return@LaunchedEffect

    musicPageViewModel.getColorPaletteFromArtwork(currentMusicState.uri)
  }

  BackHandler {
    if (bottomSheetScaffoldState.bottomSheetState.hasExpandedState) {
      coroutineScope.launch {
        bottomSheetScaffoldState.bottomSheetState.hide()
      }
    }
  }

  var bottomSheetSwapFraction = remember(screenHeight) {
    derivedStateOf {
      with(density) {
        var swapOffset =
          screenHeightPx - miniPlayerHeight.toPx() - bottomBarHeight.toPx() - runCatching { bottomSheetScaffoldState.bottomSheetState.requireOffset() }.getOrDefault(
            0f
          )
        (swapOffset / 100).coerceIn(0f, 1f)
      }
    }
  }

  Scaffold(
    bottomBar = {
      MusicNavigationBar(
        modifier = Modifier
          .onGloballyPositioned {
            bottomBarHeight = density.run { it.size.height.toDp() }
          }
          .graphicsLayer {
            translationY = density.run { bottomBarHeight.toPx() } * bottomSheetSwapFraction.value
          },
        navController = navController,
        navigateTo = {
          navController.navigate(it) {
            this.popUpTo(navController.graph.findStartDestination().id) {
              saveState = true
            }
            restoreState = true
            this.launchSingleTop = true
          }
        },
      )
    },
  ) {

    BottomSheetScaffold(
      modifier = Modifier
        .padding(top = it.calculateTopPadding())
        .consumeWindowInsets(it),
      scaffoldState = bottomSheetScaffoldState,
      sheetDragHandle = { null },
      sheetPeekHeight = miniPlayerHeight + bottomBarHeight,
      sheetMaxWidth = Dp.Unspecified,
      sheetContent = {
        AnimatedVisibility(
          visible = currentMusicState.mediaId.isNotEmpty(),
          enter = fadeIn(tween(200, delayMillis = 90)) + slideInVertically(
            animationSpec = tween(400, 250),
            initialOffsetY = { int -> int / 2 }),
          exit = slideOutVertically(
            animationSpec = tween(400, 100),
            targetOffsetY = { int -> int / 2 }) + fadeOut(tween(200, delayMillis = 90))
        ) {
          Box {
            FullMusicPlayer(
              modifier = Modifier
                .fillMaxSize()
                .alpha(bottomSheetSwapFraction.value)
                .drawWithCache {
                  onDrawBehind {
                    drawRect(Color.Black)
                    drawRect(
                      Brush.verticalGradient(
                        0.4f to musicArtWorkColorAnimation.copy(alpha = 0.8f),
                        0.7f to musicArtWorkColorAnimation.copy(alpha = 0.3f),
                        1f to Color.Black,
                      )
                    )
                  }
                }
                .navigationBarsPadding()
                .padding(top = MINI_PLAYER_HEIGHT),
              isFavorite = currentMusicState.mediaId in favoriteSongsMediaId,
              pagerMusicList = artworkPagerList.toImmutableList(),
              repeatMode = currentMusicState.repeatMode,
              currentPagerPage = currentArtworkPagerIndex,
              onPlayerAction = musicPageViewModel::onPlayerAction,
              currentVolume = currentDeviceVolume,
              mediaPlayerState = { currentMusicState },
              setCurrentPagerNumber = { musicPageViewModel.onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it)) },
              onBack = {
                coroutineScope.launch {
                  bottomSheetScaffoldState.bottomSheetState.partialExpand()
                }
              },
              currentMusicPosition = { currentMusicPosition },
              onVolumeChange = { musicPageViewModel.setDeviceVolume(it) },
              maxDeviceVolume = musicPageViewModel.getMaxDeviceVolume(),
            )
            MiniMusicPlayer(
              modifier = Modifier
                .fillMaxWidth()
                .height(miniPlayerHeight)
                .align(Alignment.TopCenter)
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .alpha(1f - bottomSheetSwapFraction.value)
                .drawWithCache {
                  onDrawBehind {
                    drawRoundRect(
                      color = Color.Black,
                      cornerRadius = CornerRadius(x = 25f, y = 25f),
                    )
                    drawRoundRect(
                      color = musicArtWorkColorAnimation,
                      cornerRadius = CornerRadius(x = 25f, y = 25f),
                      alpha = 0.3f,
                    )
                  }
                },
              onClick = {
                coroutineScope.launch {
                  bottomSheetScaffoldState.bottomSheetState.expand()
                }
              },
              artworkPagerList = artworkPagerList.toImmutableList(),
              currentMusicPosition = { currentMusicPosition },
              currentPagerPage = currentArtworkPagerIndex,
              setCurrentPagerNumber = { musicPageViewModel.onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it)) },
              onPlayerAction = musicPageViewModel::onPlayerAction,
              currentPlayerMediaId = currentMusicState.mediaId.toLong(),
              currentPlayerDuration = currentMusicState.metaData.extras?.getInt(Constant.DURATION_KEY) ?: 0,
              currentPlayerArtworkUri = currentMusicState.metaData.artworkUri,
              isPlayerPlaying = currentMusicState.isPlaying,
            )
          }
        }
      },
      sheetContainerColor = Color.Transparent,
      containerColor = Color.Transparent,
      sheetShadowElevation = 0.dp,
    ) { bottomSheetScaffoldPadding ->
      SharedTransitionLayout {

        NavHost(
          modifier = Modifier
            .fillMaxSize()
            .then(
              if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.displayCutoutPadding() else Modifier
            ),
          navController = navController,
          startDestination = MusicNavigationModel.Home,
        ) {
          composable<MusicNavigationModel.Home> {

            HomeMusic(
              listBottomPadding = miniPlayerHeight + bottomBarHeight + 5.dp,
              navigateToCategoryPage = { navController.navigate(MusicNavigationModel.Category(it, ParentRoute.FOLDER)) },
              sortState = { sortState },
              onNavigateToVideoScreen = { onNavigateToVideoScreen() },
              favoriteList = favoriteSongsList.toImmutableList(),
              favoriteSongsMediaId = favoriteSongsMediaId.toImmutableList(),
              currentPlayerMediaId = { currentMusicState.mediaId },
              currentPlayerPlayingState = { currentMusicState.isPlaying },
              isLoading = musicPageViewModel.isLoading,
              musicList = musicPageViewModel.musicList.toImmutableList(),
              folderList = folder,
              tabBarState = musicPageViewModel.tabBarState,
              animatedVisibilityScope = this,
              onTabBarClick = {
                musicPageViewModel.tabBarState = it
              },
              onItemClick = { index, list ->
                musicPageViewModel.playMusic(index, list)
              },
              onSortClick = {
                musicPageViewModel.updateSortType(it)
                musicPageViewModel.sortMusicListByCategory()
              },
              onOrderClick = {
                musicPageViewModel.updateSortIsDec(!sortState.isDec)
                musicPageViewModel.sortMusicListByCategory()
              },
            )

          }

          composable<MusicNavigationModel.Artist> {
            ArtistPage(
              modifier = Modifier,
              listItems = artist,
              bottomLazyListPadding = miniPlayerHeight + bottomBarHeight + 5.dp,
              animatedVisibilityScope = this@composable,
              navigateTo = {
                navController.navigate(MusicNavigationModel.Category(it, ParentRoute.ARTIST))
              }
            )
          }

          composable<MusicNavigationModel.Album> {
            AlbumPage(
              modifier = Modifier,
              listItems = album,
              bottomLazyListPadding = miniPlayerHeight + bottomBarHeight + 5.dp,
              animatedVisibilityScope = this@composable,
              navigateTo = {
                navController.navigate(MusicNavigationModel.Category(it, ParentRoute.ALBUM))
              }
            )
          }

          composable<MusicNavigationModel.Search> {
            val searchList by musicPageViewModel.searchList.collectAsStateWithLifecycle()

            SearchPage(
              modifier = Modifier.imePadding(),
              searchList = searchList,
              bottomLazyListPadding = miniPlayerHeight + bottomBarHeight + 5.dp,
              favoriteMusicMediaIdList = { emptyList() },
              currentPlayerMediaId = { currentMusicState.mediaId },
              currentPlayerPlayingState = { currentMusicState.isPlaying },
              onItemClick = { index ->
                musicPageViewModel.playMusic(index, searchList)
              },
              onSearch = {
                musicPageViewModel.searchMusic(it)
              },
            )
          }

          composable<MusicNavigationModel.Category> { backStackEntry ->
            // for name of parent such as folder name or artist name ...
            val parentName = backStackEntry.toRoute<MusicNavigationModel.Category>().name
            // for example if user in folder or artist page ...
            val parentRoute = backStackEntry.toRoute<MusicNavigationModel.Category>().parentRoute

            val itemList = remember(parentName) {
              when (parentRoute) {
                ParentRoute.FOLDER -> folder.first { it.categoryName == parentName }.categoryList
                ParentRoute.ARTIST -> artist.first { it.categoryName == parentName }.categoryList
                ParentRoute.ALBUM -> album.first { it.categoryName == parentName }.categoryList
              }
            }

            CategoryPage(
              name = parentName,
              itemList = itemList,
              currentMediaPlayerState = currentMusicState,
              onMusicClick = { index ->
                musicPageViewModel.playMusic(
                  index = index,
                  musicList = itemList,
                )
              },
              miniPlayerHeight = miniPlayerHeight,
              onBackClick = navController::popBackStack,
              animatedVisibilityScope = this,
            )
          }
        }

      }
    }
  }

}
