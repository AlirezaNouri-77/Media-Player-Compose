package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryLists
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.component.Loading
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.CategorySection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.TopBarMusic
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeMusic(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  musicList: List<MusicModel>,
  searchList: List<MusicModel>,
  categoryList: CategoryLists,
  tabBarState: TabBarModel,
  listBottomPadding: Dp,
  onTabBarClick: (TabBarModel) -> Unit,
  onSearch: (String) -> Unit,
  favoriteMusicMediaIdList: () -> List<String>,
  currentMusicState: () -> MediaPlayerState,
  navigateTo: (MusicNavigationModel) -> Unit,
  sortState: () -> SortState,
  onNavigateToVideoScreen: () -> Unit,
  onItemClick: (Int, List<MusicModel>) -> Unit,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
) {

  val density = LocalDensity.current

  var isKeyboardFocus by remember { mutableStateOf(false) }
  var showSearch by remember { mutableStateOf(false) }
  var showSortBar by remember { mutableStateOf(false) }
  val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })
  var listState = rememberLazyListState()
  var tabBarHeight = remember { mutableStateOf(0.dp) }

  var shouldHideTabBar = remember {
    derivedStateOf {
      listState.isScrollInProgress && listState.firstVisibleItemIndex >= 3
    }
  }

  var favoriteList = remember {
    derivedStateOf {
      musicList.filter { musicModel -> musicModel.musicId.toString() in favoriteMusicMediaIdList() }
    }
  }

  LaunchedEffect(pagerState.settledPage) {
    var currentTab = when (pagerState.currentPage) {
      0 -> TabBarModel.HOME
      1 -> TabBarModel.ARTIST
      2 -> TabBarModel.ALBUM
      3 -> TabBarModel.Folder
      4 -> TabBarModel.FAVORITE
      else -> TabBarModel.HOME
    }
    onTabBarClick(currentTab)
  }

  LaunchedEffect(tabBarState) {
    pagerState.animateScrollToPage(tabBarState.id)
  }

  Scaffold(
    modifier = modifier,
    topBar = {
      TopBarMusic(
        currentTabPosition = tabBarState,
        onVideoIconClick = { onNavigateToVideoScreen() },
        onSearch = { onSearch(it) },
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
        onKeyboardFocusChange = {
          isKeyboardFocus = it
        },
        isSearchShow = { showSearch },
        onSearchClick = { showSearch = it },
        onDismissSearch = { showSearch = false },
      )
    },
  ) { paddingValue ->

    Crossfade(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValue),
      targetState = showSearch,
      label = "",
    ) {
      if (!it) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
          var (list, category) = createRefs()

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
                  .fillMaxSize()
                  .constrainAs(list) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                  },
                state = pagerState,
                key = { it },
              ) { page ->

                if (page == 0 || page == 4) {

                  var list =
                    remember(favoriteMusicMediaIdList().size) { if (page == 0) musicList else favoriteList.value }

                  if (list.isNotEmpty()) {
                    LazyColumn(
                      state = listState,
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
                          currentMediaId = currentMusicState().mediaId,
                          onItemClick = {
                            onItemClick(index, list)
                          },
                          isPlaying = { currentMusicState().isPlaying },
                        )
                      }
                    }

                  } else EmptyPage()

                } else {
                  var list = remember {
                    if (page == 1) {
                      categoryList.artist
                    } else if (page == 2) {
                      categoryList.album
                    } else {
                      categoryList.folder
                    }
                  }
                  if (list.isNotEmpty()) {
                    LazyColumn(
                      state = listState,
                      modifier = Modifier
                        .fillMaxSize(),
                      contentPadding = PaddingValues(bottom = listBottomPadding, top = tabBarHeight.value + 8.dp),
                    ) {
                      items(
                        items = list,
                        key = { it.categoryName.hashCode() }
                      ) { item ->
                        CategoryListItem(
                          categoryName = item.categoryName,
                          musicListSize = item.categoryList.size,
                          onClick = { categoryName ->
                            navigateTo(MusicNavigationModel.Category(categoryName))
                          },
                        )
                      }
                    }

                  } else EmptyPage()

                }


              }
            }
          }

          AnimatedVisibility(
            modifier = Modifier.constrainAs(category) {
              top.linkTo(parent.top)
              start.linkTo(parent.start)
              end.linkTo(parent.end)
            },
            visible = showSearch == false or shouldHideTabBar.value,
            enter = fadeIn(tween(100)) + slideInVertically(tween(130, 90, easing = LinearEasing)) { -it / 3 },
            exit = slideOutVertically(tween(130, easing = LinearEasing)) { -it / 3 } + fadeOut(tween(100, 50))
          ) {
            CategorySection(
              modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .onPlaced { it ->
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

      } else {

        SearchPage(
          modifier = Modifier.imePadding(),
          searchList = searchList,
          favoriteMusicMediaIdList = favoriteMusicMediaIdList,
          bottomPaddingList = if (isKeyboardFocus) 0.dp else listBottomPadding,
          currentMusicState = { currentMusicState() },
          onItemClick = { index ->
            onItemClick(index, searchList)
          },
        )

      }
    }


  }

}
