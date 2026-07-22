package com.shermanrex.shermbeat.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.shermanrex.core.designsystem.util.DeviceSize
import com.shermanrex.core.designsystem.util.MiniPlayerHeight
import com.shermanrex.core.designsystem.util.NavigationBottomBarHeight
import com.shermanrex.core.designsystem.util.calculateWidthWindowSize
import com.shermanrex.core.model.MediaCategory
import com.shermanrex.feature.music_album.AlbumRoute
import com.shermanrex.feature.music_artist.ArtistRoute
import com.shermanrex.feature.music_categorydetail.CategoryDetailRoute
import com.shermanrex.feature.music_categorydetail.CategoryViewModel
import com.shermanrex.feature.music_home.HomeScreen
import com.shermanrex.feature.music_player.PlayerActions
import com.shermanrex.feature.music_player.PlayerViewModel
import com.shermanrex.feature.music_search.SearchRoute
import com.shermanrex.shermbeat.presentation.bottomBar.MusicNavigationBar
import com.shermanrex.shermbeat.presentation.component.BottomSheetContent
import com.shermanrex.shermbeat.presentation.component.LandscapePlayerComponent
import com.shermanrex.shermbeat.presentation.navigation.AlbumMusic
import com.shermanrex.shermbeat.presentation.navigation.ArtistMusic
import com.shermanrex.shermbeat.presentation.navigation.BackStackHandler
import com.shermanrex.shermbeat.presentation.navigation.DetailMusic
import com.shermanrex.shermbeat.presentation.navigation.HomeMusic
import com.shermanrex.shermbeat.presentation.navigation.NavigationRailComponent
import com.shermanrex.shermbeat.presentation.navigation.SearchMusic
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MusicMain(
    navigateToVideo: () -> Unit,
) {
    val navBackStack = retain { BackStackHandler<NavKey>(HomeMusic) }
    val playerViewModel: PlayerViewModel = koinViewModel()

    val density: Density = LocalDensity.current
    val screenHeight: Int = LocalWindowInfo.current.containerSize.height

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val windowSize = calculateWidthWindowSize()

    val playerUiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()

    val bottomPadding = if (windowSize == DeviceSize.COMPACT) {
        WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + NavigationBottomBarHeight
    } else {
        10.dp
    }

    val sheetPeekHeight = if (windowSize == DeviceSize.COMPACT) {
        bottomPadding + if (playerUiState.currentPlayerState.playingMusicInfo.musicID.isNotEmpty()) MiniPlayerHeight else 0.dp
    } else {
        0.dp
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
            modifier = it.then(if (windowSize != DeviceSize.COMPACT) Modifier.navigationBarsPadding() else Modifier),
            bottomBar = {
                MusicNavigationBar(
                    isVisible = (windowSize == DeviceSize.COMPACT),
                    navigateTo = { route -> navBackStack.addTopLevel(route, false) },
                    bottomSheetSwapFraction = { bottomSheetSwapFraction },
                    topLevel = navBackStack.topLevelKey,
                )
            },
            contentWindowInsets = WindowInsets.navigationBars,
            content = { innerPadding ->
                BottomSheetScaffold(
                    modifier = Modifier.consumeWindowInsets(innerPadding),
                    scaffoldState = bottomSheetScaffoldState,
                    sheetDragHandle = null,
                    sheetPeekHeight = sheetPeekHeight,
                    sheetContainerColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    sheetMaxWidth = Dp.Unspecified,
                    sheetShadowElevation = 0.dp,
                    sheetContent = {
                        BottomSheetContent(
                            isVisible = playerUiState.currentPlayerState.playingMusicInfo.musicID.isNotEmpty() && windowSize == DeviceSize.COMPACT,
                            playerUiState = playerUiState,
                            playerViewModel = playerViewModel,
                            currentMusicPlayerPosition = playerUiState.currentPlayerPosition,
                            currentArtworkPagerIndex = playerUiState.currentThumbnailPagerIndex,
                            currentDeviceVolume = playerUiState.currentDeviceVolume,
                            bottomSheetScaffoldState = bottomSheetScaffoldState,
                            pagerThumbnailList = playerUiState.thumbnailsList.toImmutableList(),
                            artworkDominateColor = playerUiState.thumbnailDominantColor,
                            bottomSheetSwapFraction = { bottomSheetSwapFraction },
                            navigateToArtist = { artist ->
                                navBackStack.add(DetailMusic(artist, MediaCategory.ARTIST))
                            },
                        )
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NavigationRailComponent(
                            isVisible = windowSize != DeviceSize.COMPACT,
                            topLevel = navBackStack.topLevelKey,
                            onClick = { route -> navBackStack.addTopLevel(route, false) },
                        )
                        NavDisplay(
                            modifier = Modifier.weight(0.5f),
                            backStack = navBackStack.backStack,
                            onBack = { navBackStack.removeLast() },
                            predictivePopTransitionSpec = { fadeIn() togetherWith fadeOut() },
                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator(),
                            ),
                            entryProvider = entryProvider {
                                entry<HomeMusic> {
                                    HomeScreen(
                                        onNavigateToVideoScreen = navigateToVideo,
                                        onMusicClick = { index, list ->
                                            playerViewModel.onPlayerAction(
                                                PlayerActions.PlaySongs(
                                                    index,
                                                    list,
                                                ),
                                            )
                                        },
                                        navigateToCategoryPage = { name ->
                                            navBackStack.add(
                                                DetailMusic(
                                                    name,
                                                    MediaCategory.FOLDER,
                                                ),
                                            )
                                        },
                                    )
                                }
                                entry<ArtistMusic> {
                                    ArtistRoute(
                                        PlayingMusicInfo = playerUiState.currentPlayerState.playingMusicInfo,
                                        navigateToCategory = { name ->
                                            navBackStack.add(
                                                DetailMusic(
                                                    name,
                                                    MediaCategory.ARTIST,
                                                ),
                                            )
                                        },
                                    )
                                }
                                entry<SearchMusic> {
                                    SearchRoute(
                                        onMusicClick = { index, list ->
                                            playerViewModel.onPlayerAction(
                                                PlayerActions.PlaySongs(
                                                    index,
                                                    list,
                                                ),
                                            )
                                        },
                                    )
                                }
                                entry<AlbumMusic> {
                                    AlbumRoute(
                                        PlayingMusicInfo = playerUiState.currentPlayerState.playingMusicInfo,
                                        navigateToCategory = { name ->
                                            navBackStack.add(DetailMusic(name, MediaCategory.ALBUM))
                                        },
                                    )
                                }
                                entry<DetailMusic> { param ->
                                    val categoryViewModel: CategoryViewModel =
                                        koinViewModel<CategoryViewModel>(
                                            parameters = {
                                                parametersOf(param.name, param.mediaCategory)
                                            },
                                        )

                                    val categoryUiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

                                    CategoryDetailRoute(
                                        categoryUiState = categoryUiState,
                                        categoryName = param.name,
                                        displayWithVisuals = param.displayWithVisuals,
                                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                        onBackClick = { navBackStack.removeLast() },
                                        onMusicClick = { index, list ->
                                            playerViewModel.onPlayerAction(
                                                PlayerActions.PlaySongs(
                                                    index,
                                                    list,
                                                ),
                                            )
                                        },
                                    )
                                }
                            },
                        )
                        Spacer(Modifier.width(4.dp))
                        LandscapePlayerComponent(
                            modifier = Modifier.weight(0.5f),
                            playerUiState = playerUiState,
                            isVisible = windowSize != DeviceSize.COMPACT && playerUiState.currentPlayerState.playingMusicInfo.musicID.isNotEmpty(),
                            setCurrentPagerIndex = { index ->
                                playerViewModel.onPlayerAction(PlayerActions.UpdateArtworkPageIndex(index))
                            },
                            onVolumeChange = { volume ->
                                playerViewModel.onPlayerAction(PlayerActions.OnVolumeChange(volume))
                            },
                            onTimerClick = { playerViewModel.onPlayerAction(PlayerActions.OnShowTimerBottomSheet) },
                            seekTo = { seekPosition ->
                                playerViewModel.onPlayerAction(PlayerActions.SeekTo(seekPosition))
                            },
                            onMoveToIndexPager = { index, musicId ->
                                playerViewModel.onPlayerAction(PlayerActions.OnMoveToMedia(index, musicId))
                            },
                            onFavoriteClick = { musicId ->
                                playerViewModel.onPlayerAction(PlayerActions.OnFavoriteToggle(musicId))
                            },
                            onPreviousClick = {
                                playerViewModel.onPlayerAction(PlayerActions.OnPreviousMusic(false))
                            },
                            onPauseMusic = { playerViewModel.onPlayerAction(PlayerActions.PausePlayer) },
                            onResumeMusic = { playerViewModel.onPlayerAction(PlayerActions.ResumePlayer) },
                            onNextClick = { playerViewModel.onPlayerAction(PlayerActions.OnNextMusic) },
                            onRepeatMode = { repeatMode ->
                                playerViewModel.onPlayerAction(PlayerActions.OnRepeatMode(repeatMode))
                            },
                            onShuffleModeClick = { playerViewModel.onPlayerAction(PlayerActions.OnSetShuffleMode) },
                            onArtistClick = { artist ->
                                navBackStack.add(
                                    DetailMusic(artist, MediaCategory.ARTIST),
                                )
                            },
                        )
                    }
                }
            },
        )
    }
}
