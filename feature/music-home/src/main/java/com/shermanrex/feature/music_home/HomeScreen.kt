package com.shermanrex.feature.music_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shermanrex.core.designsystem.LoadingComponent
import com.shermanrex.core.designsystem.MainTopAppBar
import com.shermanrex.core.designsystem.R
import com.shermanrex.core.designsystem.Sort
import com.shermanrex.core.designsystem.music.CategoryListComponent
import com.shermanrex.core.designsystem.music.MusicListComponent
import com.shermanrex.core.designsystem.util.rememberLazyListState
import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.datastore.CategorizedSortType
import com.shermanrex.core.model.datastore.SongsSortType
import com.shermanrex.feature.music_home.component.CategorySection
import com.shermanrex.feature.music_home.model.TabBarModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun SharedTransitionScope.HomeScreen(
    navigateToCategoryPage: (String) -> Unit,
    onNavigateToVideoScreen: () -> Unit,
    onMusicClick: (Int, List<MusicModel>) -> Unit,
) {
    val homeViewModel: HomeViewModel = koinViewModel<HomeViewModel>()
    val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })

    val uiState by homeViewModel.homeScreenUiState.collectAsStateWithLifecycle()

    LaunchedEffect(pagerState.settledPage) {
        val currentTab = when (pagerState.currentPage) {
            0 -> TabBarModel.All
            1 -> TabBarModel.Favorite
            2 -> TabBarModel.Folder
            else -> TabBarModel.All
        }
        homeViewModel.onEvent(HomeUiEvent.UpdateTabBarPosition(currentTab))
    }

    LaunchedEffect(uiState.currentTabBarPosition) {
        pagerState.animateScrollToPage(uiState.currentTabBarPosition.id)
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                modifier = Modifier,
                title = "Home",
                actions = {
                    IconButton(
                        onClick = onNavigateToVideoScreen,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.icon_video),
                            contentDescription = "video Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    AnimatedVisibility(uiState.currentTabBarPosition != TabBarModel.Favorite) {
                        Sort(
                            modifier = Modifier,
                            isDropDownMenuSortExpand = uiState.isSortDropDownMenuShow,
                            sortTypeList = if (uiState.currentTabBarPosition == TabBarModel.All) SongsSortType.entries else CategorizedSortType.entries,
                            isOrderDec = if (uiState.currentTabBarPosition == TabBarModel.All) uiState.songsSortState.isDec else uiState.folderSortState.isDec,
                            sortType = if (uiState.currentTabBarPosition == TabBarModel.All) uiState.songsSortState.sortType else uiState.folderSortState.sortType,
                            onSortClick = {
                                homeViewModel.onEvent(
                                    HomeUiEvent.UpdateSortState(
                                        uiState.currentTabBarPosition,
                                        it,
                                    ),
                                )
                            },
                            onOrderClick = {
                                homeViewModel.onEvent(HomeUiEvent.UpdateSortOrder(uiState.currentTabBarPosition))
                            },
                            onDismissDropDownMenu = { homeViewModel.onEvent(HomeUiEvent.HideSortDropDownMenu) },
                            onClick = { homeViewModel.onEvent(HomeUiEvent.ShowSortDropDownMenu) },
                        )
                    }
                },
            )
        },
    ) { paddingValue ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .consumeWindowInsets(paddingValue),
        ) {
            CategorySection(
                modifier = Modifier.fillMaxWidth(),
                currentTabState = uiState.currentTabBarPosition,
                onTabClick = { tabBar, _ ->
                    homeViewModel.onEvent(HomeUiEvent.UpdateTabBarPosition(tabBar))
                },
            )
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = uiState.isLoading,
                label = "",
            ) { isLoading ->
                if (isLoading) {
                    LoadingComponent(modifier = Modifier)
                } else {
                    HomeMusicPager(
                        currentTabBarIndex = uiState.currentTabBarPosition.id,
                        songsList = uiState.songsList.toImmutableList(),
                        favoritesList = uiState.favoritesList.toImmutableList(),
                        folderSongsList = uiState.folderSongsList.toImmutableList(),
                        isPlaying = uiState.playingMusicState.isPlaying,
                        currentMusicId = uiState.playingMusicState.playingMusicInfo.musicID,
                        allMusicInitialPagerIndex = uiState.lastHomeScrollState,
                        favoriteMusicInitialPagerIndex = uiState.lastFavoriteScrollState,
                        folderMusicInitialPagerIndex = uiState.lastFolderScrollState,
                        navigateToCategoryPage = navigateToCategoryPage,
                        onMusicClick = onMusicClick,
                        onEvent = homeViewModel::onEvent,
                    )
                }
            }
        }
    }
}

@Composable
fun SharedTransitionScope.HomeMusicPager(
    modifier: Modifier = Modifier,
    currentTabBarIndex: Int,
    songsList: ImmutableList<MusicModel>,
    favoritesList: ImmutableList<MusicModel>,
    folderSongsList: ImmutableList<Pair<String, List<MusicModel>>>,
    currentMusicId: String,
    isPlaying: Boolean,
    allMusicInitialPagerIndex: Int,
    favoriteMusicInitialPagerIndex: Int,
    folderMusicInitialPagerIndex: Int,
    onMusicClick: (Int, List<MusicModel>) -> Unit,
    navigateToCategoryPage: (String) -> Unit,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })

    LaunchedEffect(pagerState.settledPage) {
        val currentTab = when (pagerState.currentPage) {
            0 -> TabBarModel.All
            1 -> TabBarModel.Favorite
            2 -> TabBarModel.Folder
            else -> TabBarModel.All
        }
        onEvent(HomeUiEvent.UpdateTabBarPosition(currentTab))
    }

    LaunchedEffect(currentTabBarIndex) {
        pagerState.animateScrollToPage(currentTabBarIndex)
    }

    HorizontalPager(
        modifier = modifier.fillMaxSize(),
        state = pagerState,
        key = { it },
    ) { page ->

        when (page) {
            0 -> {
                val listState = rememberLazyListState(allMusicInitialPagerIndex) {
                    onEvent(HomeUiEvent.UpdateHomeScrollIndex(it))
                }
                MusicListComponent(
                    lazyListState = listState,
                    listItem = songsList,
                    isPlaying = isPlaying,
                    currentMusicId = currentMusicId,
                    onMusicClick = onMusicClick,
                )
            }

            1 -> {
                val listState = rememberLazyListState(favoriteMusicInitialPagerIndex) {
                    onEvent(HomeUiEvent.UpdateFavoriteScrollIndex(it))
                }
                MusicListComponent(
                    lazyListState = listState,
                    listItem = favoritesList,
                    isPlaying = isPlaying,
                    currentMusicId = currentMusicId,
                    onMusicClick = onMusicClick,
                )
            }

            2 -> {
                val listState = rememberLazyListState(folderMusicInitialPagerIndex) {
                    onEvent(HomeUiEvent.UpdateFolderScrollIndex(it))
                }

                CategoryListComponent(
                    listItem = folderSongsList,
                    lazyListState = listState,
                    currentMusicId = currentMusicId,
                    sharedTransitionScope = this@HomeMusicPager,
                    navigateToCategoryPage = navigateToCategoryPage,
                )
            }
        }
    }
}
