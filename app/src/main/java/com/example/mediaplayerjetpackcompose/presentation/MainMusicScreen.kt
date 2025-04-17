package com.example.mediaplayerjetpackcompose.presentation

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
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.model.navigation.MusicNavigationRoute
import com.example.core.model.navigation.ParentCategoryRoute
import com.example.core.music_media3.PlayerStateModel
import com.example.feature.music_album.AlbumRoute
import com.example.feature.music_artist.ArtistRoute
import com.example.feature.music_categorydetail.CategoryDetailRoute
import com.example.feature.music_home.HomeMusic
import com.example.feature.music_player.PlayerActions
import com.example.feature.music_player.PlayerViewModel
import com.example.feature.music_player.fullScreen.FullMusicPlayer
import com.example.feature.music_player.miniPlayer.MiniMusicPlayer
import com.example.feature.music_search.SearchRoot
import com.example.mediaplayerjetpackcompose.presentation.bottomBar.MusicNavigationBar
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainMusicScreen(
  playerViewModel: PlayerViewModel = koinViewModel(),
  onNavigateToVideoScreen: () -> Unit,
  density: Density = LocalDensity.current,
  orientation: Int = LocalConfiguration.current.orientation,
  screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp,
) {

  val navController: NavHostController = rememberNavController()

  val bottomSheetState = rememberStandardBottomSheetState()
  val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

  val coroutineScope = rememberCoroutineScope()

  val currentMusicState by playerViewModel.currentMusicState.collectAsStateWithLifecycle()

  val currentMusicPosition by playerViewModel.currentMusicPosition.collectAsStateWithLifecycle()
  val currentDeviceVolume by playerViewModel.currentDeviceVolume.collectAsStateWithLifecycle()
  val currentArtworkPagerIndex by playerViewModel.currentArtworkPagerIndex.collectAsStateWithLifecycle()
  val artworkPagerList by playerViewModel.artworkPagerList.collectAsStateWithLifecycle()
  val favoriteSongsMediaId by playerViewModel.favoriteSongsMediaId.collectAsStateWithLifecycle()

  var bottomNavBarHeight by remember { mutableStateOf(0.dp) }

  val bottomPadding by remember(bottomNavBarHeight) {
    mutableStateOf(playerViewModel.miniPlayerHeight + bottomNavBarHeight + 5.dp)
  }

  val screenHeightPx = remember(screenHeight) {
    with(density) {
      screenHeight.toPx()
    }
  }

  val musicArtWorkColorAnimation by animateColorAsState(
    targetValue = Color(playerViewModel.musicArtworkColorPalette),
    animationSpec = tween(durationMillis = 90, delayMillis = 40),
    label = "",
  )

  // when music id change ( mean the music change ) this block code trigger
  // and get palette color for mini player and full screen player
  LaunchedEffect(key1 = currentMusicState.currentMediaInfo.musicID) {
    if (currentMusicState.currentMediaInfo.musicID.isEmpty()) return@LaunchedEffect

    playerViewModel.getColorPaletteFromArtwork(currentMusicState.currentMediaInfo.musicUri.toUri())
  }

  BackHandler {
    if (bottomSheetState.hasExpandedState) {
      coroutineScope.launch {
        bottomSheetScaffoldState.bottomSheetState.partialExpand()
      }
    }
  }

  // used for hide or show mini player and bottomNavBar on bottomSheet interaction
  val bottomSheetSwapFraction = remember(screenHeight) {
    derivedStateOf {
      with(density) {
        val swapOffset =
          screenHeightPx - playerViewModel.miniPlayerHeight.toPx() - bottomNavBarHeight.toPx() - runCatching { bottomSheetScaffoldState.bottomSheetState.requireOffset() }.getOrDefault(
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
            bottomNavBarHeight = density.run { it.size.height.toDp() }
          }
          .graphicsLayer {
            translationY = density.run { bottomNavBarHeight.toPx() } * bottomSheetSwapFraction.value
          },
        navController = navController,
        navigateTo = {
          val currentDestination = navController.currentDestination?.id ?: navController.graph.findStartDestination().id
          navController.navigate(it) {
            this.popUpTo(currentDestination) {
              inclusive = true
              saveState = true
            }
            restoreState = true
            this.launchSingleTop = true
          }
        },
      )
    },
  ) {

    CompositionLocalProvider(LocalBottomPadding provides bottomPadding) {

      BottomSheetScaffold(
        modifier = Modifier
          .padding(top = it.calculateTopPadding())
          .consumeWindowInsets(it),
        scaffoldState = bottomSheetScaffoldState,
        sheetDragHandle = {},
        sheetPeekHeight = playerViewModel.miniPlayerHeight + bottomNavBarHeight,
        sheetMaxWidth = Dp.Unspecified,
        sheetContent = {
          AnimatedVisibility(
            visible = currentMusicState.currentMediaInfo.musicID.isNotEmpty(),
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
                  .padding(top = playerViewModel.miniPlayerHeight),
                isFavorite = currentMusicState.currentMediaInfo.musicID in favoriteSongsMediaId,
                pagerMusicList = artworkPagerList.toImmutableList(),
                repeatMode = currentMusicState.repeatMode,
                currentPagerPage = currentArtworkPagerIndex,
                onPlayerAction = playerViewModel::onPlayerAction,
                currentVolume = currentDeviceVolume,
                playerStateModel = { currentMusicState },
                setCurrentPagerNumber = {
                  playerViewModel.onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it))
                },
                onBack = {
                  coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                  }
                },
                currentMusicPosition = { currentMusicPosition },
                onVolumeChange = { playerViewModel.setDeviceVolume(it) },
                maxDeviceVolume = playerViewModel.getMaxDeviceVolume(),
              )
              MiniMusicPlayer(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(playerViewModel.miniPlayerHeight)
                  .align(Alignment.TopCenter)
                  .padding(horizontal = 8.dp, vertical = 2.dp)
                  .alpha(1f - bottomSheetSwapFraction.value),
                onClick = {
                  coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                  }
                },
                artworkPagerList = artworkPagerList.toImmutableList(),
                currentMusicPosition = { currentMusicPosition },
                currentPagerPage = currentArtworkPagerIndex,
                setCurrentPagerNumber = {
                  playerViewModel.onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it))
                },
                onPlayerAction = playerViewModel::onPlayerAction,
                currentPlayerMediaId = currentMusicState.currentMediaInfo.musicID.toLong(),
                currentPlayerDuration = currentMusicState.currentMediaInfo.duration.toInt(),
                currentPlayerArtworkUri = currentMusicState.currentMediaInfo.artworkUri.toUri(),
                isPlaying = currentMusicState.isPlaying,
                musicArtWorkColorAnimation = musicArtWorkColorAnimation,
              )
            }
          }
        },
        sheetContainerColor = Color.Transparent,
        containerColor = Color.Transparent,
        sheetShadowElevation = 0.dp,
      ) { bottomSheetScaffoldPadding ->

        MusicNavGraph(
          navController = navController,
          currentMusicState = { currentMusicState },
          onPlayMusic = {
            playerViewModel.onPlayerAction(it)
          },
          onNavigateToVideoScreen = { onNavigateToVideoScreen() },
          orientation = orientation,
        )

      }
    }

  }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MusicNavGraph(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  currentMusicState: () -> PlayerStateModel,
  onPlayMusic: (PlayerActions.PlaySongs) -> Unit,
  onNavigateToVideoScreen: () -> Unit,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  SharedTransitionLayout {

    NavHost(
      modifier = modifier
        .fillMaxSize()
        .then(
          if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.displayCutoutPadding() else Modifier
        ),
      navController = navController,
      startDestination = MusicNavigationRoute.Home,
    ) {

      composable<MusicNavigationRoute.Home> {
        HomeMusic(
          navigateToCategoryPage = { navController.navigate(MusicNavigationRoute.Category(it, ParentCategoryRoute.FOLDER)) },
          onNavigateToVideoScreen = { onNavigateToVideoScreen() },
          currentPlayerMediaId = { currentMusicState().currentMediaInfo.musicID },
          currentPlayerPlayingState = { currentMusicState().isPlaying },
          animatedVisibilityScope = this,
          onMusicClick = { index, list ->
            onPlayMusic(PlayerActions.PlaySongs(index, list))
          },
        )
      }

      composable<MusicNavigationRoute.Artist> {
        ArtistRoute(
          modifier = Modifier,
          animatedVisibilityScope = this@composable,
          navigateToCategory = {
            navController.navigate(MusicNavigationRoute.Category(it, ParentCategoryRoute.ARTIST))
          }
        )
      }

      composable<MusicNavigationRoute.Album> {
        AlbumRoute(
          modifier = Modifier,
          navigateToCategory = {
            navController.navigate(MusicNavigationRoute.Category(it, ParentCategoryRoute.ALBUM))
          },
          animatedVisibilityScope = this@composable
        )
      }

      composable<MusicNavigationRoute.Search> {
        SearchRoot(
          modifier = Modifier.imePadding(),
          currentPlayerMediaId = { currentMusicState().currentMediaInfo.musicID },
          currentPlayerPlayingState = { currentMusicState().isPlaying },
          onMusicClick = { index, list ->
            onPlayMusic(PlayerActions.PlaySongs(index, list))
          },
        )
      }

      composable<MusicNavigationRoute.Category> { backStackEntry ->
        // for name of parent such as folder name or artist name ...
        val categoryName = backStackEntry.toRoute<MusicNavigationRoute.Category>().name
        // for example if user in folder or artist page ...
        val parentRoute = backStackEntry.toRoute<MusicNavigationRoute.Category>().parentCategoryRoute

        CategoryDetailRoute(
          categoryName = categoryName,
          currentMusicId = currentMusicState().currentMediaInfo.musicID,
          isPlayerPlaying = { currentMusicState().isPlaying },
          parentCategoryRouteName = parentRoute,
          onMusicClick = { index, list ->
            onPlayMusic(PlayerActions.PlaySongs(index, list))
          },
          onBackClick = navController::popBackStack,
          animatedVisibilityScope = this,
        )
      }

    }
  }
}