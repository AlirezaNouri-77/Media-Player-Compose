package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mediaplayerjetpackcompose.designSystem.EmptyPage
import com.example.mediaplayerjetpackcompose.designSystem.Loading
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.LocalBottomPadding
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeMusic(
  modifier: Modifier = Modifier,
  homeViewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
  currentPlayerMediaId: () -> String,
  currentPlayerPlayingState: () -> Boolean,
  navigateToCategoryPage: (String) -> Unit,
  onNavigateToVideoScreen: () -> Unit,
  onMusicClick: (Int, List<MusicModel>) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  lazyListBottomPadding: Dp = LocalBottomPadding.current,
  density: Density = LocalDensity.current
) {

  var showSortBar by remember { mutableStateOf(false) }

  val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })

  val listSortState by homeViewModel.sortState.collectAsStateWithLifecycle()

  val folder by homeViewModel.folder.collectAsStateWithLifecycle()
  val favoriteSongs by homeViewModel.favoriteSongs.collectAsStateWithLifecycle()
  val favoriteSongsMediaId by homeViewModel.favoriteSongsMediaId.collectAsStateWithLifecycle()

  var listStates = TabBarModel.entries.map {
    rememberLazyListState()
  }
  var tabBarHeight by remember { mutableStateOf(0.dp) }

  var shouldHideTabBar = remember {
    derivedStateOf {
      listStates[pagerState.currentPage].isScrollInProgress
           && (listStates[pagerState.currentPage].firstVisibleItemIndex >= 3)
           && (pagerState.currentPage == 0)
    }
  }

  LaunchedEffect(pagerState.settledPage) {
    var currentTab = when (pagerState.currentPage) {
      0 -> TabBarModel.All
      1 -> TabBarModel.Favorite
      2 -> TabBarModel.Folder
      else -> TabBarModel.All
    }
    homeViewModel.tabBarState = currentTab
  }

  LaunchedEffect(homeViewModel.tabBarState) {
    pagerState.animateScrollToPage(homeViewModel.tabBarState.id)
  }

  Scaffold(
    modifier = modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
    ),
    topBar = {
      HomePageTopBar(
        currentTabPosition = homeViewModel.tabBarState,
        onVideoIconClick = { onNavigateToVideoScreen() },
        onSortIconClick = { showSortBar = true },
        isDropDownMenuSortExpand = showSortBar,
        sortState = { listSortState },
        onDismissDropDownMenu = { showSortBar = false },
        onSortClick = {
          homeViewModel.updateSortType(it)
          homeViewModel.sortMusicListByCategory()
        },
        onOrderClick = {
          homeViewModel.updateSortIsDec(!listSortState.isDec)
          homeViewModel.sortMusicListByCategory()
        },
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
        targetState = homeViewModel.isLoading,
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

              var list = remember(
                homeViewModel.musicList, favoriteSongs
              ) { if (page == 0) homeViewModel.musicList else favoriteSongs }

              if (list.isNotEmpty()) {
                LazyColumn(
                  state = listStates[page],
                  modifier = Modifier
                    .fillMaxSize(),
                  contentPadding = PaddingValues(bottom = lazyListBottomPadding, top = tabBarHeight + 8.dp),
                ) {
                  itemsIndexed(
                    items = list,
                    key = { _, item -> item.musicId },
                  ) { index, item ->
                    MusicMediaItem(
                      item = item,
                      isFav = item.musicId.toString() in favoriteSongsMediaId,
                      currentMediaId = currentPlayerMediaId(),
                      onItemClick = {
                        onMusicClick(index, list)
                      },
                      isPlaying = { currentPlayerPlayingState() },
                    )
                  }
                }
              } else EmptyPage()

            } else {

              if (folder.isNotEmpty()) {
                LazyColumn(
                  state = listStates[page],
                  modifier = Modifier
                    .fillMaxSize(),
                  contentPadding = PaddingValues(bottom = lazyListBottomPadding, top = tabBarHeight + 8.dp),
                ) {
                  items(
                    items = folder,
                    key = { it.categoryName.hashCode() }
                  ) { item ->
                    CategoryListItem(
                      categoryName = item.categoryName,
                      musicListSize = item.categoryList.size,
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
            .background(MaterialTheme.colorScheme.background)
            .onGloballyPositioned { it ->
              tabBarHeight = with(density) {
                it.size.height.toDp()
              }
            },
          currentTabState = homeViewModel.tabBarState,
          onTabClick = { tabBar, _ ->
            homeViewModel.tabBarState = tabBar
          }
        )
      }
    }

  }

}
