package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPage(
  name: String,
  itemList: List<MusicModel>,
  currentMediaPlayerState: MediaPlayerState,
  onMusicClick: (index: Int) -> Unit,
  miniPlayerHeight: Dp,
  onBackClick: () -> Unit,
) {

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            modifier = Modifier.basicMarquee(),
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
          containerColor = Color.Transparent,
          titleContentColor = MaterialTheme.colorScheme.onPrimary,
        )
      )
    },
  ) { innerPadding ->

    LazyColumn(
      Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(top = 10.dp, bottom = miniPlayerHeight)
    ) {
      itemsIndexed(
        items = itemList,
        key = { _, item -> item.musicId },
      ) { index, item ->
        MusicMediaItem(
          item = item,
          isFav = false,
          currentMediaId = currentMediaPlayerState.mediaId,
          onItemClick = {
            onMusicClick.invoke(index)
          },
          isPlaying = { currentMediaPlayerState.isPlaying },
        )
      }
    }

  }

}