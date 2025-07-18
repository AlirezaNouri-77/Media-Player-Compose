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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.core.designsystem.CategoryListItem
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.Loading
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.MainTopAppBar
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.Sort
import com.example.core.model.MusicModel
import com.example.core.model.TabBarModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.datastore.SongsSortType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
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

  var isSortDropDownMenuShow by remember { mutableStateOf(false) }
  var currentTabBarPosition by remember { mutableStateOf(TabBarModel.All) }

  val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })

  val songsSortState by homeViewModel.songSortState.collectAsStateWithLifecycle()
  val folderSortState by homeViewModel.folderSortState.collectAsStateWithLifecycle()

  val songs by homeViewModel.songsList.collectAsStateWithLifecycle()
  val folder by homeViewModel.folderSongsData.collectAsStateWithLifecycle()
  val favoriteSongs by homeViewModel.favoriteSongs.collectAsStateWithLifecycle()
//  val favoriteSongsMediaId by homeViewModel.favoriteSongsMediaId.collectAsStateWithLifecycle()

  val listStates = TabBarModel.entries.map {
    rememberLazyListState()
  }
  var tabBarHeight by remember { mutableStateOf(0.dp) }

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
    currentTabBarPosition = currentTab
  }

  LaunchedEffect(currentTabBarPosition) {
    pagerState.animateScrollToPage(currentTabBarPosition.id)
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
          VideoIconButton(
            onClick = {
              onNavigateToVideoScreen()
            },
          )
          AnimatedVisibility(currentTabBarPosition != TabBarModel.Favorite) {
            Sort(
              modifier = Modifier,
              onClick = { isSortDropDownMenuShow = true },
              sortTypeList = if (currentTabBarPosition == TabBarModel.All) SongsSortType.entries else CategorizedSortType.entries,
              onSortClick = {
                homeViewModel.updateDataStoreSortType(currentTabBarPosition, it)
              },
              onOrderClick = {
                homeViewModel.updateDataStoreOrder(currentTabBarPosition)
              },
              isDropDownMenuSortExpand = isSortDropDownMenuShow,
              isOrderDec = if (currentTabBarPosition == TabBarModel.All) songsSortState.isDec else folderSortState.isDec,
              sortType = if (currentTabBarPosition == TabBarModel.All) songsSortState.sortType else folderSortState.sortType,
              onDismissDropDownMenu = { isSortDropDownMenuShow = false },
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
              val list = remember(songs, favoriteSongs) {
                if (page == 0) songs else favoriteSongs
              }

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
            .background(MaterialTheme.colorScheme.background)
            .onGloballyPositioned { it ->
              tabBarHeight = with(density) {
                it.size.height.toDp()
              }
            },
          currentTabState = currentTabBarPosition,
          onTabClick = { tabBar, _ ->
            currentTabBarPosition = tabBar
          }
        )
      }

    }

  }

}
