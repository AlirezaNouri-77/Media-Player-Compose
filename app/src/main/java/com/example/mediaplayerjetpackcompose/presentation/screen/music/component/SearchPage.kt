package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.SearchSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(
  modifier: Modifier = Modifier,
  searchList: List<MusicModel>,
  favoriteMusicMediaIdList: () -> List<String>,
  currentPlayerMediaId: () -> String,
  currentPlayerPlayingState: () -> Boolean,
  onItemClick: (Int) -> Unit,
  onSearch: (String) -> Unit,
  bottomLazyListPadding:Dp,
) {

  val searchTextFieldValue = rememberSaveable { mutableStateOf("") }

  LaunchedEffect(
    key1 = searchTextFieldValue.value,
    block = {
      snapshotFlow { searchTextFieldValue }
        .debounce(500)
        .collectLatest {
          onSearch(it.value.trim())
        }
    },
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Search",
            modifier = Modifier,
            fontWeight = FontWeight.Bold,
            fontSize = 38.sp,
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
        )
      )
    },
  ) { innerPadding ->

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {

      SearchSection(
        textFieldValue = searchTextFieldValue.value,
        onTextFieldChange = { searchTextFieldValue.value = it },
        onClear = {
          searchTextFieldValue.value = ""
        },
      )

      if (searchList.isNotEmpty()) {
        LazyColumn(
          modifier = modifier
            .fillMaxSize(),
          contentPadding = PaddingValues(bottom = bottomLazyListPadding)
        ) {
          itemsIndexed(
            items = searchList,
            key = { _, item -> item.musicId },
          ) { index, item ->
            MusicMediaItem(
              item = item,
              isFav = item.musicId.toString() in favoriteMusicMediaIdList(),
              currentMediaId = currentPlayerMediaId(),
              onItemClick = {
                onItemClick(index)
              },
              isPlaying = { currentPlayerPlayingState() },
            )
          }
        }
      } else EmptyPage(message = "Nothing found")

    }

  }

}