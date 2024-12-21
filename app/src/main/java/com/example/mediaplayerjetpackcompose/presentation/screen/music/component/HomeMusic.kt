package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryLists
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.Loading
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.TabBarSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.TopBarMusic

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

  var isKeyboardFocus by remember { mutableStateOf(false) }

  var showSearch by remember { mutableStateOf(false) }

  var showSortBar by remember { mutableStateOf(false) }

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

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValue),
    ) {

      AnimatedVisibility(
        visible = !showSearch,
        enter = fadeIn(tween(100)) + slideInVertically(tween(130, 50, easing = LinearEasing)) { -it },
        exit = slideOutVertically(tween(130, easing = LinearEasing)) { -it } + fadeOut(tween(100, 50))
      ) {
        TabBarSection(
          currentTabState = tabBarState,
          onTabClick = { tabBar, _ ->
            onTabBarClick(tabBar)
          }
        )
      }

      Crossfade(
        modifier = Modifier
          .fillMaxSize(),
        targetState = showSearch,
        label = "",
      ) {

        if (it == false) {

          Crossfade(
            modifier = Modifier
              .fillMaxSize(),
            targetState = isLoading,
            label = "",
          ) { target ->
            if (target) {
              Loading(modifier = Modifier)
            } else {
              MusicListHandler(
                currentMusicState = { currentMusicState() },
                favoriteMusicMediaIdList = favoriteMusicMediaIdList(),
                bottomPadding = listBottomPadding,
                navigateTo = {
                  navigateTo(it)
                },
                musicList = musicList,
                categoryLists = categoryList,
                currentTabState = { tabBarState },
                onTabBarChange = {
                  onTabBarClick(it)
                },
                onItemClick = { index, list ->
                  onItemClick(index, list)
                },
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

}
