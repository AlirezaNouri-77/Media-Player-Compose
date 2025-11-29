package com.example.mediaplayerjetpackcompose.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.core.designsystem.LocalParentScaffoldPadding
import com.example.core.designsystem.MiniPlayerHeight
import com.example.core.designsystem.NavigationBottomBarHeight
import com.example.core.model.MediaCategory
import com.example.feature.music_album.AlbumRoute
import com.example.feature.music_artist.ArtistRoute
import com.example.feature.music_categorydetail.CategoryDetailRoute
import com.example.feature.music_categorydetail.CategoryViewModel
import com.example.feature.music_home.HomeMusic
import com.example.feature.music_player.PlayerActions
import com.example.feature.music_player.PlayerViewModel
import com.example.feature.music_search.SearchRoot
import com.example.mediaplayerjetpackcompose.presentation.bottomBar.MusicNavigationBar
import com.example.mediaplayerjetpackcompose.presentation.component.BottomSheetContent
import com.example.mediaplayerjetpackcompose.presentation.navigation.AlbumMusic
import com.example.mediaplayerjetpackcompose.presentation.navigation.ArtistMusic
import com.example.mediaplayerjetpackcompose.presentation.navigation.BackStackHandler
import com.example.mediaplayerjetpackcompose.presentation.navigation.DetailMusic
import com.example.mediaplayerjetpackcompose.presentation.navigation.HomeMusic
import com.example.mediaplayerjetpackcompose.presentation.navigation.NavigationRailComponent
import com.example.mediaplayerjetpackcompose.presentation.navigation.SearchMusic
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MusicMain(
    navigateToVideo: () -> Unit,
) {
    val backStack = remember { BackStackHandler<NavKey>(HomeMusic) }
    val playerViewModel: PlayerViewModel = koinViewModel()

    val density: Density = LocalDensity.current
    val screenHeight: Int = LocalWindowInfo.current.containerSize.height

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val windowSize = currentWindowAdaptiveInfo()

    val uiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()
    val windowInset = WindowInsets.systemBars.asPaddingValues()

    val bottomPadding = remember(windowInset.calculateBottomPadding(), windowSize) {
        if (windowSize.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            windowInset.calculateBottomPadding() + NavigationBottomBarHeight
        } else {
            10.dp
        }
    }

    BackHandler {
        if (bottomSheetScaffoldState.bottomSheetState.hasExpandedState) {
            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }
        }
    }

    // used for hide or show mini player and bottomNavBar on bottomSheet interaction start from 0 to 1
    val bottomSheetSwapFraction by remember(screenHeight, bottomPadding) {
        derivedStateOf {
            with(density) {
                val swapOffset =
                    screenHeight - MiniPlayerHeight.toPx() - bottomPadding.toPx() - runCatching {
                        bottomSheetScaffoldState.bottomSheetState.requireOffset()
                    }.getOrDefault(0f)
                (swapOffset / 100).coerceIn(0f, 1f)
            }
        }
    }

    SharedTransitionScope {
        Scaffold(
            modifier = it,
            bottomBar = {
                MusicNavigationBar(
                    isVisible = windowSize.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT,
                    navigateTo = { route -> backStack.addTopLevel(route, true) },
                    bottomBarGradientColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    bottomSheetSwapFraction = { bottomSheetSwapFraction },
                    topLevel = backStack.topLevelKey,
                )
            },
            contentWindowInsets = WindowInsets.navigationBars,
            content = { innerPadding ->
                BottomSheetScaffold(
                    modifier = Modifier,
                    scaffoldState = bottomSheetScaffoldState,
                    sheetDragHandle = {},
                    sheetPeekHeight = bottomPadding + if (uiState.currentPlayerState.currentMediaInfo.musicID.isNotEmpty()) MiniPlayerHeight else 0.dp,
                    sheetContainerColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    sheetTonalElevation = 0.dp,
                    sheetShadowElevation = 0.dp,
                    sheetMaxWidth = Int.MAX_VALUE.dp,
                    sheetContent = {
                        BottomSheetContent(
                            isVisible = uiState.currentPlayerState.currentMediaInfo.musicID.isNotEmpty(),
                            currentMusicState = uiState.currentPlayerState,
                            playerViewModel = playerViewModel,
                            currentMusicPlayerPosition = uiState.currentPlayerPosition,
                            currentArtworkPagerIndex = uiState.currentThumbnailPagerIndex,
                            currentDeviceVolume = uiState.currentDeviceVolume,
                            bottomSheetScaffoldState = bottomSheetScaffoldState,
                            pagerThumbnailList = uiState.thumbnailsList,
                            artworkDominateColor = uiState.thumbnailDominantColor,
                            bottomSheetSwapFraction = { bottomSheetSwapFraction },
                            navigateToArtist = {
                                backStack.add(DetailMusic(it, MediaCategory.ARTIST))
                            },
                        )
                    },
                ) {
                    Row {
                        NavigationRailComponent(
                            isVisible = windowSize.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT,
                            topLevel = backStack.topLevelKey,
                            onClick = { },
                        )
                        NavDisplay(
                            backStack = backStack.backStack,
                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator(),
                            ),
                            entryProvider = entryProvider {
                                CompositionLocalProvider(
                                    LocalParentScaffoldPadding provides innerPadding,
                                ) {
                                    entry<HomeMusic> {
                                        HomeMusic(
                                            navigateToCategoryPage = {},
                                            onNavigateToVideoScreen = navigateToVideo,
                                            onMusicClick = { index, list ->
                                                playerViewModel.onPlayerAction(PlayerActions.PlaySongs(index, list))
                                            },
                                        )
                                    }
                                    entry<ArtistMusic> {
                                        ArtistRoute(
                                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                            navigateToCategory = { name ->
                                                backStack.add(DetailMusic(name, MediaCategory.ARTIST))
                                            },
                                        )
                                    }
                                    entry<SearchMusic> {
                                        SearchRoot(
                                            modifier = Modifier.imePadding(),
                                            onMusicClick = { index, list ->
                                                playerViewModel.onPlayerAction(PlayerActions.PlaySongs(index, list))
                                            },
                                        )
                                    }
                                    entry<AlbumMusic> {
                                        AlbumRoute(
                                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                            navigateToCategory = { name ->
                                                backStack.add(DetailMusic(name, MediaCategory.ALBUM))
                                            },
                                        )
                                    }
                                    entry<DetailMusic> { param ->
                                        val categoryViewModel: CategoryViewModel = koinViewModel<CategoryViewModel>(
                                            parameters = {
                                                parametersOf(param.name, param.mediaCategory)
                                            },
                                        )

                                        CategoryDetailRoute(
                                            categoryViewModel = categoryViewModel,
                                            categoryName = param.name,
                                            displayWithVisuals = param.displayWithVisuals,
                                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                            onBackClick = { backStack.removeLast() },
                                            onMusicClick = { index, list ->
                                                playerViewModel.onPlayerAction(PlayerActions.PlaySongs(index, list))
                                            },
                                        )
                                    }
                                }
                            },
                        )
                    }
                }
            },
        )
    }
}
