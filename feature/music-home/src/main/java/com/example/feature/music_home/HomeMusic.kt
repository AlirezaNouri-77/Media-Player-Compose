package com.example.feature.music_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.CategoryListItem
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.Loading
import com.example.core.designsystem.MainTopAppBar
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.R
import com.example.core.designsystem.Sort
import com.example.core.model.MusicModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.datastore.SongsSortType
import com.example.feature.music_home.component.CategorySection
import com.example.feature.music_home.component.categoryHeight
import com.example.feature.music_home.model.TabBarModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.HomeMusic(
  modifier: Modifier = Modifier,
  homeViewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
  currentPlayerMediaId: String,
  isPlaying: Boolean,
  navigateToCategoryPage: (String) -> Unit,
  onNavigateToVideoScreen: () -> Unit,
  onMusicClick: (Int, List<MusicModel>) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
) {
  val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })

  val uiState by homeViewModel.homeScreenUiState.collectAsStateWithLifecycle()

  val listStates = TabBarModel.entries.map { rememberLazyListState() }

  val shouldHideTabBar = remember {
    derivedStateOf {
      listStates[pagerState.currentPage].isScrollInProgress
           && (listStates[pagerState.currentPage].firstVisibleItemIndex >= 3)
           && (pagerState.currentPage == 0)
    }
  }

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
    modifier = modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
    ),
    topBar = {
      MainTopAppBar(
        modifier = Modifier,
        title = "Home",
        actions = {
          IconButton(
            onClick = onNavigateToVideoScreen,
          ) {
            Icon(
              modifier = modifier.size(24.dp),
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
              onSortClick = { homeViewModel.onEvent(HomeUiEvent.UpdateSortState(uiState.currentTabBarPosition, it)) },
              onOrderClick = { homeViewModel.onEvent(HomeUiEvent.UpdateSortOrder(uiState.currentTabBarPosition)) },
              isOrderDec = if (uiState.currentTabBarPosition == TabBarModel.All) uiState.songsSortState.isDec else uiState.folderSortState.isDec,
              sortType = if (uiState.currentTabBarPosition == TabBarModel.All) uiState.songsSortState.sortType else uiState.folderSortState.sortType,
              onClick = { homeViewModel.onEvent(HomeUiEvent.ShowSortDropDownMenu) },
              onDismissDropDownMenu = { homeViewModel.onEvent(HomeUiEvent.HideSortDropDownMenu) },
            )
          }
        }
      )
    },
  ) { paddingValue ->

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValue),
    ) {

      Crossfade(
        modifier = Modifier
          .fillMaxSize(),
        targetState = uiState.isLoading,
        label = "",
      ) { target ->
        if (target) {
          Loading(modifier = Modifier)
        } else {
          HorizontalPager(
            modifier = Modifier
              .fillMaxSize(),
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
                  contentPadding = PaddingValues(top = categoryHeight.dp + 8.dp),
                ) {
                  itemsIndexed(
                    items = list,
                    key = { _, item -> item.musicId },
                  ) { index, item ->
                    MusicMediaItem(
                      item = item,
                      currentMediaId = currentPlayerMediaId,
                      onItemClick = { onMusicClick(index, list) },
                      isPlaying = { isPlaying },
                    )
                  }
                }
              } else EmptyPage()
            } else {
              if (uiState.folderSongsList.isNotEmpty()) {
                LazyColumn(
                  state = listStates[page],
                  modifier = Modifier
                    .fillMaxSize(),
                  contentPadding = PaddingValues(top = categoryHeight.dp),
                ) {
                  items(
                    items = uiState.folderSongsList,
                    key = { it }
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
              } else EmptyPage()
            }
          }
        }
      }

      AnimatedVisibility(
        modifier = Modifier.align(Alignment.TopCenter),
        visible = !shouldHideTabBar.value,
        enter = fadeIn(tween(100)) + slideInVertically(tween(130, 90, easing = LinearEasing)) { -it / 3 },
        exit = slideOutVertically(tween(130, easing = LinearEasing)) { -it / 3 } + fadeOut(tween(100, 50))
      ) {
        CategorySection(
          modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
          currentTabState = uiState.currentTabBarPosition,
          onTabClick = { tabBar, _ ->
            homeViewModel.onEvent(HomeUiEvent.UpdateTabBarPosition(tabBar))
          }
        )
      }

    }
  }
}
