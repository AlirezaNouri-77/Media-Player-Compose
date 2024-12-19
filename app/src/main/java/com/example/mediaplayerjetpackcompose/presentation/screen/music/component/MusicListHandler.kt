package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryLists
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@Composable
fun MusicListHandler(
  currentMusicState: () -> MediaPlayerState,
  musicList: List<MusicModel>,
  categoryLists: CategoryLists,
  currentTabState: () -> TabBarModel,
  onTabBarChange: (TabBarModel) -> Unit,
  navigateTo: (MusicNavigationModel) -> Unit,
  onItemClick: (itemIndex: Int, data: List<MusicModel>) -> Unit,
  favoriteMusicMediaIdList: List<String>,
  bottomPadding: Dp,
) {

  val pagerState = rememberPagerState(pageCount = { TabBarModel.entries.size })

  LaunchedEffect(pagerState.settledPage) {
    var currentTab = when (pagerState.currentPage) {
      0 -> TabBarModel.MUSIC
      1 -> TabBarModel.ARTIST
      2 -> TabBarModel.ALBUM
      3 -> TabBarModel.Folder
      4 -> TabBarModel.FAVORITE
      else -> TabBarModel.MUSIC
    }
    onTabBarChange(currentTab)
  }

  LaunchedEffect(currentTabState()) {
    pagerState.animateScrollToPage(currentTabState().id)
  }

  HorizontalPager(
    state = pagerState,
    key = { it },
  ) { page ->

    when (page) {
      0, 4 -> {
        val listItem = remember(page) {
          when (page) {
            0 -> musicList
            4 -> musicList.filter { musicModel -> musicModel.musicId.toString() in favoriteMusicMediaIdList }
            else -> emptyList()
          }
        }

        if (listItem.isNotEmpty()) {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize(),
            contentPadding = PaddingValues(bottom = bottomPadding, top = 10.dp),
          ) {
            itemsIndexed(
              items = listItem,
              key = { _, item -> item.musicId },
            ) { index, item ->
              MusicMediaItem(
                item = item,
                isFav = item.musicId.toString() in favoriteMusicMediaIdList,
                currentMediaId = currentMusicState().mediaId,
                onItemClick = {
                  onItemClick(index, musicList)

                },
                isPlaying = currentMusicState().isPlaying,
              )
            }
          }
        } else {
          EmptyPage()
        }

      }

      1, 2, 3 -> {

        val listItem = remember(page) {
          when (page) {
            1 -> categoryLists.artist
            2 -> categoryLists.album
            3 -> categoryLists.folder
            else -> emptyList()
          }
        }

        if (listItem.isNotEmpty()) {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize(),
            contentPadding = PaddingValues(bottom = bottomPadding, top = 10.dp),
          ) {
            items(
              items = listItem,
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
        } else {
          EmptyPage()
        }

      }
    }
  }
}