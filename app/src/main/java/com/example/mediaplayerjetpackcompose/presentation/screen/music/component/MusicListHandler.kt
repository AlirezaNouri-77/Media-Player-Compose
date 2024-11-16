package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@Composable
fun MusicListHandler(
  musicPageViewModel: MusicPageViewModel,
  currentMusicState: CurrentMediaState,
  navController: NavController,
  favoriteMusicMediaIdList: List<String>,
  bottomPadding: Dp,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  val favoriteMusicData = remember(favoriteMusicMediaIdList.size) {
    musicPageViewModel.musicList.filter { musicModel -> musicModel.musicId.toString() in favoriteMusicMediaIdList }
      .toMutableStateList()
  }

  Crossfade(
    targetState = musicPageViewModel.currentTabState,
    label = "",
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .then(
          if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.displayCutoutPadding() else Modifier
        ),
      contentPadding = PaddingValues(bottom = bottomPadding, top = 10.dp),
    ) {
      when (it) {
        TabBarPosition.MUSIC -> {
          itemsIndexed(
            items = musicPageViewModel.musicList,
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

        TabBarPosition.FAVORITE -> {


          if (favoriteMusicData.isNotEmpty()) {
            itemsIndexed(
              items = favoriteMusicData,
              key = { _, item -> item.musicId },
            ) { index, item ->
              MusicMediaItem(
                item = item,
                isFav = item.musicId.toString() in favoriteMusicMediaIdList,
                currentMediaId = currentMusicState.mediaId,
                onItemClick = {
                  musicPageViewModel.playMusic(
                    index = index,
                    musicList = favoriteMusicData
                  )
                },
                isPlaying = currentMusicState.isPlaying,
              )
            }
          }


        }

        TabBarPosition.ARTIST -> {
          items(musicPageViewModel.artistsMusicMap) { item ->
            CategoryListItem(
              categoryName = item.categoryName,
              musicListSize = item.categoryList.size,
              onClick = { categoryName ->
                navController.navigate(MusicNavigationModel.Category(categoryName))
              },
            )
          }
        }

        TabBarPosition.ALBUM -> {
          items(musicPageViewModel.albumMusicMap) { item ->
            CategoryListItem(
              categoryName = item.categoryName,
              musicListSize = item.categoryList.size,
              onClick = { categoryName ->
                navController.navigate(MusicNavigationModel.Category(categoryName))
              },
            )
          }
        }

      }
    }
  }
}