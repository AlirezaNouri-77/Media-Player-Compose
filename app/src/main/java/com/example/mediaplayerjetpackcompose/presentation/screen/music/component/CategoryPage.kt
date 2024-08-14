package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.TabBarPosition
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
    modifier = Modifier.statusBarsPadding(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = name,
            fontSize = 18.sp,
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
      )
    },
  ) { innerPadding ->

    LazyColumn(Modifier.fillMaxSize().padding(innerPadding)) {
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