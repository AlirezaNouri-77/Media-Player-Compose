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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@Composable
fun MusicListHandler(
  musicPageViewModel: MusicPageViewModel,
  currentMusicState: CurrentMediaState,
  navigateTo: (MusicNavigationModel) -> Unit,
  favoriteMusicMediaIdList: List<String>,
  bottomPadding: Dp,
) {

  val pagerState = rememberPagerState(pageCount = { TabBarPosition.entries.size })

  LaunchedEffect(pagerState.settledPage) {
    musicPageViewModel.currentTabState = when (pagerState.currentPage) {
      0 -> TabBarPosition.MUSIC
      1 -> TabBarPosition.ARTIST
      2 -> TabBarPosition.ALBUM
      3 -> TabBarPosition.Folder
      4 -> TabBarPosition.FAVORITE
      else -> TabBarPosition.MUSIC
    }
  }

  LaunchedEffect(musicPageViewModel.currentTabState) {
    pagerState.animateScrollToPage(musicPageViewModel.currentTabState.id)
  }

  HorizontalPager(
    state = pagerState,
    key = { it },
  ) { page ->

    when (page) {
      0, 4 -> {
        val listItem = remember(page) {
          when (page) {
            0 -> musicPageViewModel.musicList
            4 -> musicPageViewModel.musicList.filter { musicModel -> musicModel.musicId.toString() in favoriteMusicMediaIdList }
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
                currentMediaId = currentMusicState.mediaId,
                onItemClick = {
                  musicPageViewModel.playMusic(
                    index = index,
                    musicPageViewModel.musicList
                  )
                },
                isPlaying = currentMusicState.isPlaying,
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
            1 -> musicPageViewModel.artistsMusicMap
            2 -> musicPageViewModel.albumMusicMap
            3 -> musicPageViewModel.folderMusicMap
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