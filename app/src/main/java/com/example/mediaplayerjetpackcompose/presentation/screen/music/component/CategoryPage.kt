package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.TabBarPosition
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.sortBar
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPage(
  name: String,
  musicPageViewModel: MusicPageViewModel,
  currentMediaCurrentState: MediaCurrentState,
  onMusicClick: (index: Int, musicList: List<MusicModel>) -> Unit,
  onBackClick: () -> Unit,
) {

  musicPageViewModel.musicCategoryList = remember(musicPageViewModel.currentTabState) {
    when (musicPageViewModel.currentTabState) {
      TabBarPosition.ARTIST -> musicPageViewModel.artistsMusicMap.first { it.categoryName == name }.categoryList.toMutableStateList()
      TabBarPosition.ALBUM -> musicPageViewModel.albumMusicMap.first { it.categoryName == name }.categoryList.toMutableStateList()
      else -> emptyList<MusicModel>().toMutableStateList()
    }
  }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
          )
        },
        navigationIcon = {
          IconButton(onClick = { onBackClick.invoke() }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
              contentDescription = "",
              modifier = Modifier
                .size(35.dp),
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        windowInsets = WindowInsets(top = 0.dp)
      )
    },
    contentWindowInsets = WindowInsets(bottom = 0),
  ) {
    Column(modifier = Modifier.padding(it)) {
      LazyRow(
        modifier = Modifier
          .padding(vertical = 15.dp)
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
                resultList as SnapshotStateList<MusicModel>
            }
          },
          onDecClick = {
            musicPageViewModel.isDec.value = !musicPageViewModel.isDec.value
            musicPageViewModel.sortMusicListByCategory(musicPageViewModel.musicCategoryList)
              .also { resultList ->
                musicPageViewModel.musicCategoryList =
                  resultList as SnapshotStateList<MusicModel>
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
            isFav = false,
            currentMediaId = currentMediaCurrentState.mediaId,
            onItemClick = {
              onMusicClick.invoke(index, musicPageViewModel.musicCategoryList)
            },
          )
        }
      }

    }
  }

}