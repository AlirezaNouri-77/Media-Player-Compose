package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

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
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryLists
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.component.Loading
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.CategorySection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.HomePageTopBar
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeMusic(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  musicList: List<MusicModel>,
  categoryList: CategoryLists,
  tabBarState: TabBarModel,
  listBottomPadding: Dp,
  onTabBarClick: (TabBarModel) -> Unit,
  favoriteMusicMediaIdList: () -> List<String>,
  currentPlayerMediaId: () -> String,
  currentPlayerPlayingState: () -> Boolean,
  navigateToCategoryPage: (String) -> Unit,
  sortState: () -> SortState,
  onNavigateToVideoScreen: () -> Unit,
  onItemClick: (Int, List<MusicModel>) -> Unit,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  density: Density = LocalDensity.current
) {

  var showSortBar by remember { mutableStateOf(false) }
  val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })
  var listStates = TabBarModel.entries.map {
    rememberLazyListState()
  }
  var tabBarHeight = remember { mutableStateOf(0.dp) }

  var shouldHideTabBar = remember {
    derivedStateOf {
      listStates[pagerState.currentPage].isScrollInProgress
           && (listStates[pagerState.currentPage].firstVisibleItemIndex >= 3)
           && (pagerState.currentPage == 0)
    }
  }

  var favoriteList = remember {
    derivedStateOf {
      musicList.filter { musicModel -> musicModel.musicId.toString() in favoriteMusicMediaIdList() }
    }
  }

  LaunchedEffect(pagerState.settledPage) {
    var currentTab = when (pagerState.currentPage) {
      0 -> TabBarModel.All
      1 -> TabBarModel.Favorite
      2 -> TabBarModel.Folder
      else -> TabBarModel.All
    }
    onTabBarClick(currentTab)
  }

  LaunchedEffect(tabBarState) {
    pagerState.animateScrollToPage(tabBarState.id)
  }

  Scaffold(
    modifier = modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
    ),
    topBar = {
      HomePageTopBar(
        currentTabPosition = tabBarState,
        onVideoIconClick = { onNavigateToVideoScreen() },
        onSortIconClick = { showSortBar = true },
        isDropDownMenuSortExpand = showSortBar,
        sortState = { sortState() },
        onDismissDropDownMenu = { showSortBar = false },
        onSortClick = {
          onSortClick(it)
        },
        onOrderClick = {
          onOrderClick()
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
        targetState = isLoading,
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

              var list = remember { if (page == 0) musicList else favoriteList.value }

              if (list.isNotEmpty()) {
                LazyColumn(
                  state = listStates[page],
                  modifier = Modifier
                    .fillMaxSize(),
                  contentPadding = PaddingValues(bottom = listBottomPadding, top = tabBarHeight.value + 8.dp),
                ) {
                  itemsIndexed(
                    items = list,
                    key = { _, item -> item.musicId },
                  ) { index, item ->
                    MusicMediaItem(
                      item = item,
                      isFav = item.musicId.toString() in favoriteMusicMediaIdList(),
                      currentMediaId = currentPlayerMediaId(),
                      onItemClick = {
                        onItemClick(index, musicList)
                      },
                      isPlaying = { currentPlayerPlayingState() },
                    )
                  }
                }
              } else EmptyPage()

            } else {

              if (categoryList.folder.isNotEmpty()) {
                LazyColumn(
                  state = listStates[page],
                  modifier = Modifier
                    .fillMaxSize(),
                  contentPadding = PaddingValues(bottom = listBottomPadding, top = tabBarHeight.value + 8.dp),
                ) {
                  items(
                    items = categoryList.folder,
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
              tabBarHeight.value = with(density) {
                it.size.height.toDp()
              }
            },
          currentTabState = tabBarState,
          onTabClick = { tabBar, _ ->
            onTabBarClick(tabBar)
          }
        )
      }
    }

  }

}
