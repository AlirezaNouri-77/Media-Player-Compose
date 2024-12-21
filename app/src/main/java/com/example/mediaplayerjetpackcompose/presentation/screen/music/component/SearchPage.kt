package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@Composable
fun SearchPage(
  modifier: Modifier = Modifier,
  searchList: List<MusicModel>,
  favoriteMusicMediaIdList: () -> List<String>,
  currentMusicState: () -> MediaPlayerState,
  onItemClick: (Int) -> Unit,
  bottomPaddingList: Dp,
) {
  if (searchList.isNotEmpty()) {
    LazyColumn(
      modifier = modifier
        .fillMaxSize(),
      contentPadding = PaddingValues(bottom = bottomPaddingList, top = 10.dp),
    ) {
      itemsIndexed(
        items = searchList,
        key = { _, item -> item.musicId },
      ) { index, item ->
        MusicMediaItem(
          item = item,
          isFav = item.musicId.toString() in favoriteMusicMediaIdList(),
          currentMediaId = currentMusicState().mediaId,
          onItemClick = {
            onItemClick(index)
          },
          isPlaying = currentMusicState().isPlaying,
        )
      }
    }
  } else EmptyPage(message = "Nothing found")
}