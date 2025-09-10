package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.core.designsystem.LocalMiniPlayerHeight
import com.example.core.designsystem.LocalParentScaffoldPadding
import com.example.core.designsystem.MiniPlayerHeight
import com.example.feature.music_player.PlayerViewModel
import com.example.mediaplayerjetpackcompose.presentation.bottomBar.MusicNavigationBar
import com.example.mediaplayerjetpackcompose.presentation.bottomBar.NavigationBarModel
import com.example.mediaplayerjetpackcompose.presentation.component.BottomSheetContent
import com.example.mediaplayerjetpackcompose.presentation.mainVideoGraph
import com.example.mediaplayerjetpackcompose.presentation.musicNavGraph
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MainNavGraph() {
    val playerViewModel: PlayerViewModel = koinViewModel()

    val density: Density = LocalDensity.current
    val screenHeight: Int = LocalWindowInfo.current.containerSize.height
    val navController = rememberNavController()

    val bottomSheetState = rememberStandardBottomSheetState()
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val coroutineScope = rememberCoroutineScope()

    val bottomBarGradientColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val windowSize = currentWindowAdaptiveInfo()

    val uiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()

    var bottomBarHeight by remember { mutableIntStateOf(0) }

    // when music id change ( mean the music change ) this block code trigger
    // and get palette color for mini player and full screen player
    LaunchedEffect(key1 = uiState.currentPlayerState.currentMediaInfo.musicID) {
        if (uiState.currentPlayerState.currentMediaInfo.musicID.isEmpty()) return@LaunchedEffect

        playerViewModel.getColorPaletteFromArtwork(uiState.currentPlayerState.currentMediaInfo.musicUri.toUri())
    }

    val isInMusicScreen = remember(backStackEntry) {
        backStackEntry?.destination?.hierarchy?.any { it.hasRoute(MusicTopLevelDestination.Parent::class) }
            ?: false
    }

    BackHandler {
        if (bottomSheetState.hasExpandedState) {
            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }
        }
    }

    // used for hide or show mini player and bottomNavBar on bottomSheet interaction start from 0 to 1
    val bottomSheetSwapFraction by remember(screenHeight) {
        derivedStateOf {
            with(density) {
                val swapOffset =
                    screenHeight - MiniPlayerHeight.toPx() - bottomBarHeight - runCatching {
                        bottomSheetScaffoldState.bottomSheetState.requireOffset()
                    }.getOrDefault(0f)
                (swapOffset / 100).coerceIn(0f, 1f)
            }
        }
    }

    Scaffold(
        bottomBar = {
            MusicNavigationBar(
                isVisible = isInMusicScreen && windowSize.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT,
                modifier = Modifier.onGloballyPositioned { bottomBarHeight = it.size.height },
                navController = navController,
                navigateTo = { navController.navigationBarNavigate(backStackEntry, it) },
                bottomBarGradientColor = bottomBarGradientColor,
                bottomSheetSwapFraction = bottomSheetSwapFraction,
            )
        },
        contentWindowInsets = WindowInsets.navigationBars,
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalParentScaffoldPadding provides innerPadding,
            LocalMiniPlayerHeight provides MiniPlayerHeight,
        ) {
            BottomSheetScaffold(
                modifier = Modifier,
                scaffoldState = bottomSheetScaffoldState,
                sheetDragHandle = {},
                sheetPeekHeight = innerPadding.calculateBottomPadding() + if (uiState.currentPlayerState.currentMediaInfo.musicID.isNotEmpty()) LocalMiniPlayerHeight.current else 0.dp,
                sheetContainerColor = Color.Transparent,
                containerColor = Color.Transparent,
                sheetTonalElevation = 0.dp,
                sheetShadowElevation = 0.dp,
                sheetMaxWidth = Int.MAX_VALUE.dp,
                sheetContent = {
                    BottomSheetContent(
                        navController = navController,
                        isVisible = uiState.currentPlayerState.currentMediaInfo.musicID.isNotEmpty() && isInMusicScreen,
                        currentMusicState = uiState.currentPlayerState,
                        playerViewModel = playerViewModel,
                        currentMusicPlayerPosition = uiState.currentPlayerPosition,
                        currentArtworkPagerIndex = uiState.currentThumbnailPagerIndex,
                        currentDeviceVolume = uiState.currentDeviceVolume,
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        pagerThumbnailList = uiState.thumbnailsList,
                        bottomSheetSwapFraction = { bottomSheetSwapFraction },
                        artworkDominateColor = uiState.thumbnailDominantColor,
                    )
                },
            ) {
                Row {
                    NavigationRailComponent(
                        isVisible = windowSize.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT && isInMusicScreen,
                        navController = navController,
                        onClick = { navController.navigationBarNavigate(backStackEntry, it) },
                    )
                    SharedTransitionLayout {
                        NavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = MusicTopLevelDestination.Parent,
                        ) {
                            mainVideoGraph(
                                onBack = navController::popBackStack,
                                navController = navController,
                            )
                            musicNavGraph(
                                playerViewModel = playerViewModel,
                                navController = navController,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                onNavigateToVideoScreen = {
                                    navController.navigate(
                                        VideoTopLevelDestination.VideoPage,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationRailComponent(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    navController: NavHostController,
    onClick: (MusicTopLevelDestination) -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    if (isVisible) {
        NavigationRail(
            containerColor = Color.Transparent,
            modifier = modifier.displayCutoutPadding(),
        ) {
            NavigationBarModel.entries.forEachIndexed { index, item ->
                val isSelected =
                    NavigationBarModel.entries.any {
                        backStackEntry?.destination?.hasRoute(item.route::class) == true
                    }
                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onClick(item.route) },
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(item.title) },
                    alwaysShowLabel = true,
                    colors = NavigationRailItemDefaults.colors(
                        indicatorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        selectedIconColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        unselectedIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    ),
                )
            }
        }
    }
}
