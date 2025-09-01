package com.example.mediaplayerjetpackcompose.presentation

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
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
import com.example.core.designsystem.LocalMiniPlayerHeight
import com.example.core.designsystem.LocalParentScaffoldPadding
import com.example.core.designsystem.MiniPlayerHeight
import com.example.core.model.navigation.MusicNavigationRoute
import com.example.core.model.navigation.ParentCategoryRoute
import com.example.core.music_media3.model.PlayerStateModel
import com.example.feature.music_album.AlbumRoute
import com.example.feature.music_artist.ArtistRoute
import com.example.feature.music_categorydetail.CategoryDetailRoute
import com.example.feature.music_categorydetail.CategoryViewModel
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
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainMusicScreen(
    playerViewModel: PlayerViewModel = koinViewModel(),
    onNavigateToVideoScreen: () -> Unit,
    density: Density = LocalDensity.current,
    screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp,
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
) {
    val navController: NavHostController = rememberNavController()
    val bottomSheetState = rememberStandardBottomSheetState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val coroutineScope = rememberCoroutineScope()

    val bottomBarGradientColor = if (isSystemInDarkTheme()) Color.Black else Color.White

    val currentMusicState by playerViewModel.currentMusicState.collectAsStateWithLifecycle()
    val currentMusicPosition by playerViewModel.currentMusicPosition.collectAsStateWithLifecycle()
    val currentDeviceVolume by playerViewModel.currentDeviceVolume.collectAsStateWithLifecycle()
    val currentArtworkPagerIndex by playerViewModel.currentArtworkPagerIndex.collectAsStateWithLifecycle()
    val artworkPagerList by playerViewModel.artworkPagerList.collectAsStateWithLifecycle()

    val musicArtWorkColorAnimation by animateColorAsState(
        targetValue = Color(playerViewModel.musicArtworkColorPalette),
        animationSpec = tween(durationMillis = 160, delayMillis = 80, easing = LinearEasing),
        label = "",
    )

    var bottomBarHeight by remember {
        mutableIntStateOf(0)
    }

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

    // used for hide or show mini player and bottomNavBar on bottomSheet interaction start from 0 to 1
    val bottomSheetSwapFraction by remember(screenHeight, screenWidth) {
        derivedStateOf {
            with(density) {
                val swapOffset =
                    screenHeight.toPx() - MiniPlayerHeight.toPx() - bottomBarHeight - runCatching {
                        bottomSheetScaffoldState.bottomSheetState.requireOffset()
                    }.getOrDefault(
                        0f,
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
                        bottomBarHeight = it.size.height
                    }
                    .drawWithCache {
                        onDrawBehind {
                            drawContext
                            drawRect(
                                brush = Brush.verticalGradient(
                                    0.1f to Color.Transparent,
                                    0.5f to bottomBarGradientColor.copy(alpha = 0.7f),
                                    1f to bottomBarGradientColor.copy(alpha = 0.9f),
                                    endY = this.size.height,
                                ),
                                alpha = 1f - bottomSheetSwapFraction,
                                topLeft = Offset(
                                    x = 0f,
                                    y = this.size.height * bottomSheetSwapFraction,
                                ),
                            )
                        }
                    }
                    .graphicsLayer {
                        translationY = this@graphicsLayer.size.height * bottomSheetSwapFraction
                    },
                navController = navController,
                navigateTo = {
                    val currentDestination = navController.currentDestination?.id
                        ?: navController.graph.findStartDestination().id
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
        contentWindowInsets = WindowInsets.navigationBars,
    ) { innerPadding ->

        CompositionLocalProvider(
            LocalParentScaffoldPadding provides innerPadding,
            LocalMiniPlayerHeight provides MiniPlayerHeight,
        ) {
            BottomSheetScaffold(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .navigationBarsPadding()
                    .consumeWindowInsets(innerPadding),
                scaffoldState = bottomSheetScaffoldState,
                sheetDragHandle = {},
                sheetPeekHeight = innerPadding.calculateBottomPadding() + if (currentMusicState.currentMediaInfo.musicID.isNotEmpty()) LocalMiniPlayerHeight.current else 0.dp,
                sheetContent = {
                    AnimatedVisibility(
                        visible = currentMusicState.currentMediaInfo.musicID.isNotEmpty(),
                        enter = fadeIn(tween(200, delayMillis = 90)) + slideInVertically(
                            animationSpec = tween(400, 250),
                            initialOffsetY = { int -> int / 2 },
                        ),
                        exit = slideOutVertically(
                            animationSpec = tween(400, 100),
                            targetOffsetY = { int -> int / 2 },
                        ) + fadeOut(
                            tween(200, delayMillis = 90),
                        ),
                    ) {
                        Box {
                            FullMusicPlayer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .alpha(bottomSheetSwapFraction)
                                    .drawBehind {
                                        drawRect(Color.Black)
                                        drawRect(
                                            Brush.verticalGradient(
                                                0.4f to musicArtWorkColorAnimation.copy(alpha = 0.8f),
                                                0.85f to musicArtWorkColorAnimation.copy(alpha = 0.3f),
                                                1f to Color.Transparent,
                                            ),
                                        )
                                        drawRect(
                                            Brush.verticalGradient(
                                                0.5f to Color.Black.copy(alpha = 0.5f),
                                                1f to Color.Transparent,
                                            ),
                                        )
                                    }
                                    .navigationBarsPadding()
                                    .padding(top = LocalMiniPlayerHeight.current),
                                isFavorite = currentMusicState.currentMediaInfo.isFavorite,
                                pagerMusicList = artworkPagerList.toImmutableList(),
                                repeatMode = currentMusicState.repeatMode,
                                currentPagerPage = currentArtworkPagerIndex,
                                onPlayerAction = playerViewModel::onPlayerAction,
                                currentVolume = currentDeviceVolume,
                                playerStateModel = { currentMusicState },
                                setCurrentPagerNumber = {
                                    playerViewModel.onPlayerAction(
                                        PlayerActions.UpdateArtworkPageIndex(
                                            it,
                                        ),
                                    )
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
                                    .height(LocalMiniPlayerHeight.current)
                                    .align(Alignment.TopCenter)
                                    .padding(horizontal = 8.dp)
                                    .padding(top = 4.dp, bottom = 4.dp)
                                    .alpha(1f - bottomSheetSwapFraction),
                                onClick = {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    }
                                },
                                artworkPagerList = artworkPagerList.toImmutableList(),
                                currentMusicPosition = { currentMusicPosition },
                                currentPagerPage = currentArtworkPagerIndex,
                                setCurrentPagerNumber = {
                                    playerViewModel.onPlayerAction(
                                        PlayerActions.UpdateArtworkPageIndex(
                                            it,
                                        ),
                                    )
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
                sheetTonalElevation = 0.dp,
                sheetShadowElevation = 0.dp,
                sheetMaxWidth = Int.MAX_VALUE.dp,
            ) { bottomSheetScaffoldPadding ->

                MusicNavGraph(
                    navController = navController,
                    currentMusicState = currentMusicState,
                    onPlayMusic = {
                        playerViewModel.onPlayerAction(it)
                    },
                    onNavigateToVideoScreen = { onNavigateToVideoScreen() },
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
    currentMusicState: PlayerStateModel,
    onPlayMusic: (PlayerActions.PlaySongs) -> Unit,
    onNavigateToVideoScreen: () -> Unit,
    orientation: Int = LocalConfiguration.current.orientation,
) {
    SharedTransitionLayout {
        NavHost(
            modifier = modifier
                .fillMaxSize()
                .then(
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.displayCutoutPadding() else Modifier,
                ),
            navController = navController,
            startDestination = MusicNavigationRoute.Home,
        ) {
            composable<MusicNavigationRoute.Home> {
                HomeMusic(
                    navigateToCategoryPage = {
                        navController.navigate(
                            MusicNavigationRoute.Category(
                                it,
                                ParentCategoryRoute.FOLDER,
                                false,
                            ),
                        )
                    },
                    onNavigateToVideoScreen = { onNavigateToVideoScreen() },
                    currentPlayerMediaId = currentMusicState.currentMediaInfo.musicID,
                    isPlaying = currentMusicState.isPlaying,
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
                        navController.navigate(
                            MusicNavigationRoute.Category(
                                it,
                                ParentCategoryRoute.ARTIST,
                            ),
                        )
                    },
                )
            }

            composable<MusicNavigationRoute.Album> {
                AlbumRoute(
                    modifier = Modifier,
                    navigateToCategory = {
                        navController.navigate(
                            MusicNavigationRoute.Category(
                                it,
                                ParentCategoryRoute.ALBUM,
                            ),
                        )
                    },
                    animatedVisibilityScope = this@composable,
                )
            }

            composable<MusicNavigationRoute.Search> {
                SearchRoot(
                    modifier = Modifier.imePadding(),
                    currentPlayerMediaId = currentMusicState.currentMediaInfo.musicID,
                    currentPlayerPlayingState = currentMusicState.isPlaying,
                    onMusicClick = { index, list ->
                        onPlayMusic(
                            PlayerActions.PlaySongs(
                                index,
                                list,
                            ),
                        )
                    },
                )
            }

            composable<MusicNavigationRoute.Category> { backStackEntry ->
                // for name of parent such as folder name or artist name ...
                val categoryName = backStackEntry.toRoute<MusicNavigationRoute.Category>().name
                // for example if user in folder or artist page ...
                val parentRoute =
                    backStackEntry.toRoute<MusicNavigationRoute.Category>().parentCategoryRoute

                val displayWithVisuals =
                    backStackEntry.toRoute<MusicNavigationRoute.Category>().displayWithVisuals

                val categoryViewModel: CategoryViewModel = koinViewModel<CategoryViewModel>(
                    parameters = { parametersOf(categoryName, parentRoute, displayWithVisuals) },
                )

                CategoryDetailRoute(
                    categoryViewModel = categoryViewModel,
                    categoryName = categoryName,
                    currentMusicId = currentMusicState.currentMediaInfo.musicID,
                    isPlayerPlaying = currentMusicState.isPlaying,
                    onMusicClick = { index, list ->
                        onPlayMusic(PlayerActions.PlaySongs(index, list))
                    },
                    onBackClick = navController::popBackStack,
                    animatedVisibilityScope = this,
                    displayWithVisuals = displayWithVisuals,
                )
            }
        }
    }
}
