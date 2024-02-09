package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.MusicMediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistAlbumPage(
  name: String,
  musicPageViewModel: MusicPageViewModel,
  currentMusicState: MusicState,
  onMusicClick: (index: Int, musicList: List<MusicMediaModel>) -> Unit,
) {

  musicPageViewModel.musicCategoryList = remember(musicPageViewModel.currentTabState) {
    when (musicPageViewModel.currentTabState) {
      1 -> musicPageViewModel.artistsMusicMap[name]!!.toMutableStateList()
      2 -> musicPageViewModel.albumMusicMap[name]!!.toMutableStateList()
      else -> emptyList<MusicMediaModel>().toMutableStateList()
    }
  }

  Scaffold(
    topBar = {
      MediumTopAppBar(
        title = {
          Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
          )
        },
        navigationIcon = {
          Image(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "",
            modifier = Modifier.size(35.dp)
          )
        }
      )
    }
  ) {
    Column(modifier = Modifier.padding(it)) {
      LazyRow(
        modifier = Modifier
          .padding(horizontal = 15.dp)
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {
        sortBar(
          musicPageViewModel = musicPageViewModel,
          onSortClick = { sortItem ->
            musicPageViewModel.currentListSort.value = sortItem
            musicPageViewModel.sortMusicListByCategory(
              list = musicPageViewModel.musicCategoryList
            ).also { resultList ->
              musicPageViewModel.musicCategoryList =
                resultList as SnapshotStateList<MusicMediaModel>
            }
          },
          onDecClick = {
            musicPageViewModel.isDec.value = !musicPageViewModel.isDec.value
            musicPageViewModel.sortMusicListByCategory(musicPageViewModel.musicCategoryList)
              .also { resultList ->
                musicPageViewModel.musicCategoryList =
                  resultList as SnapshotStateList<MusicMediaModel>
              }
          },
        )
      }

      LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(
          items = musicPageViewModel.musicCategoryList,
          key = { _, item -> item.musicId },
        ) { index, item ->
          MusicMediaItem(
            item = item,
            currentMediaId = currentMusicState.mediaId,
            artworkImage = musicPageViewModel.getImageArt(item.uri),
            onItemClick = {
              onMusicClick.invoke(index, musicPageViewModel.musicCategoryList)
            },
          )
        }
      }

    }
  }

}