package com.example.feature.music_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.CategoryListItem
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.Loading
import com.example.core.designsystem.LocalMiniPlayerHeight
import com.example.core.designsystem.LocalParentScaffoldPadding
import com.example.core.designsystem.MainTopAppBar
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.R
import com.example.core.designsystem.Sort
import com.example.core.model.MusicModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.datastore.SongsSortType
import com.example.feature.music_home.component.CategorySection
import com.example.feature.music_home.model.TabBarModel
import org.koin.androidx.compose.koinViewModel

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun SharedTransitionScope.HomeMusic(
    homeViewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
    navigateToCategoryPage: (String) -> Unit,
    onNavigateToVideoScreen: () -> Unit,
    onMusicClick: (Int, List<MusicModel>) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })

    val uiState by homeViewModel.homeScreenUiState.collectAsStateWithLifecycle()

    val listStates = TabBarModel.entries.map { rememberLazyListState() }

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
                                homeViewModel.onEvent(HomeUiEvent.UpdateSortState(uiState.currentTabBarPosition, it))
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
            ) { target ->

                if (target) Loading(modifier = Modifier)

                if (!target) {
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                        key = { it },
                    ) { page ->

                        if (page == 0 || page == 1) {
                            val list = remember(uiState.songsList, uiState.favoritesList) {
                                if (page == 0) uiState.songsList else uiState.favoritesList
                            }

                            if (list.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    state = listStates[page],
                                    contentPadding = PaddingValues(
                                        bottom = LocalParentScaffoldPadding.current.calculateBottomPadding() + if (uiState.playerStateModel.currentMediaInfo.musicID.isNotEmpty()) LocalMiniPlayerHeight.current else 0.dp,
                                    ),
                                ) {
                                    itemsIndexed(
                                        items = list,
                                        key = { _, item -> item.musicId },
                                    ) { index, item ->
                                        MusicMediaItem(
                                            musicId = item.musicId,
                                            artworkUri = item.artworkUri,
                                            name = item.name,
                                            artist = item.artist,
                                            duration = item.duration,
                                            isFavorite = item.isFavorite,
                                            currentMediaId = uiState.playerStateModel.currentMediaInfo.musicID,
                                            onItemClick = { onMusicClick(index, list) },
                                            isPlaying = { uiState.playerStateModel.isPlaying },
                                        )
                                    }
                                }
                            } else {
                                EmptyPage()
                            }
                        } else {
                            if (uiState.folderSongsList.isNotEmpty()) {
                                LazyColumn(
                                    state = listStates[page],
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        bottom = LocalParentScaffoldPadding.current.calculateBottomPadding() + if (uiState.playerStateModel.currentMediaInfo.musicID.isNotEmpty()) LocalMiniPlayerHeight.current else 0.dp,
                                    ),
                                ) {
                                    items(
                                        items = uiState.folderSongsList,
                                        key = { it },
                                    ) { item ->
                                        CategoryListItem(
                                            categoryName = item.first,
                                            musicListSize = item.second.size,
                                            onClick = { categoryName ->
                                                navigateToCategoryPage(categoryName)
                                            },
                                            sharedTransitionScope = this@HomeMusic,
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                    }
                                }
                            } else {
                                EmptyPage()
                            }
                        }
                    }
                }
            }
        }
    }
}
