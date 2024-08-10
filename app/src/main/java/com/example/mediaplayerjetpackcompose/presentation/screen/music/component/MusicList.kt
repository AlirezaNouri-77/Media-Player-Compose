package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.TabBarPosition
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@Composable
fun MusicList(
  musicPageViewModel: MusicPageViewModel,
  currentMusicState: MediaCurrentState,
  navController: NavController,
  paddingValue: PaddingValues,
  bottomPadding: Dp,
) {

  Crossfade(
    targetState = musicPageViewModel.currentTabState,
    label = "",
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValue),
      contentPadding = PaddingValues(bottom = bottomPadding),
    ) {
      when (it) {
        TabBarPosition.MUSIC -> {
          itemsIndexed(
            items = musicPageViewModel.musicList,
            key = { _, item -> item.musicId },
          ) { index, item ->
            MusicMediaItem(
              item = item,
              isFav = item.musicId.toString() in musicPageViewModel.favoriteListMediaId.toList(),
              currentMediaId = currentMusicState.mediaId,
              onItemClick = {
                musicPageViewModel.playMusic(
                  index = index,
                  musicPageViewModel.musicList
                )
              },
            )
          }
        }

        TabBarPosition.FAVORITE -> {

          musicPageViewModel.favoriteMusicList =
            musicPageViewModel.musicList.filter { it.musicId.toString() in musicPageViewModel.favoriteListMediaId }
              .toMutableStateList()

          if (musicPageViewModel.favoriteMusicList.isNotEmpty()) {
            itemsIndexed(
              items = musicPageViewModel.favoriteMusicList,
              key = { _, item -> item.musicId },
            ) { index, item ->
              MusicMediaItem(
                item = item,
                isFav = item.musicId.toString() in musicPageViewModel.favoriteListMediaId.toList(),
                currentMediaId = currentMusicState.mediaId,
                onItemClick = {
                  musicPageViewModel.playMusic(
                    index = index,
                    musicPageViewModel.favoriteMusicList
                  )
                },
              )
            }
          }


        }

        TabBarPosition.ARTIST -> {
          items(musicPageViewModel.artistsMusicMap) { item ->
            CategoryListItem(
              categoryName = item.categoryName,
              musicListSize = item.categoryList.size,
              onClick = { string ->
                navController.navigate("Category/$string")
              },
            )
          }
        }

        TabBarPosition.ALBUM -> {
          items(musicPageViewModel.albumMusicMap) { item ->
            CategoryListItem(
              categoryName = item.categoryName,
              musicListSize = item.categoryList.size,
              onClick = { string ->
                navController.navigate("Category/$string")
              },
            )
          }
        }

      }
    }
  }
}