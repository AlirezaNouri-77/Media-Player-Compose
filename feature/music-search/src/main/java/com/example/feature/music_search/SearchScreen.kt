package com.example.feature.music_search


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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.MusicMediaItem
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import org.koin.androidx.compose.koinViewModel

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchRoot(
  modifier: Modifier = Modifier,
  searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
  currentPlayerMediaId: () -> String,
  currentPlayerPlayingState: () -> Boolean,
  onMusicClick: (Int, List<MusicModel>) -> Unit,
) {

  var searchListItem = searchViewModel.searchList.collectAsStateWithLifecycle()
  var favoriteMusicMediaIdList = searchViewModel.favoriteMusicMediaIds.collectAsStateWithLifecycle()

  SearchScreen(
    modifier = modifier,
    listItem = searchListItem.value.toImmutableList(),
    favoriteMusicMediaIdList = { favoriteMusicMediaIdList.value.toImmutableList() },
    currentPlayerMediaId = currentPlayerMediaId,
    currentPlayerPlayingState = { currentPlayerPlayingState() },
    onMusicClick = { index, list ->
      onMusicClick(index, list)
    },
    onSearch = {
      searchViewModel.searchMusic(it)
    },
    lazyListBottomPadding = LocalBottomPadding.current
  )


}

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  listItem: ImmutableList<MusicModel>,
  favoriteMusicMediaIdList: () -> ImmutableList<String>,
  currentPlayerMediaId: () -> String,
  currentPlayerPlayingState: () -> Boolean,
  onMusicClick: (Int, List<MusicModel>) -> Unit,
  onSearch: (String) -> Unit,
  lazyListBottomPadding: Dp,
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

      SearchTextFieldSection(
        textFieldValue = searchTextFieldValue.value,
        onTextFieldChange = { searchTextFieldValue.value = it },
        onClear = {
          searchTextFieldValue.value = ""
        },
      )

      if (listItem.isNotEmpty()) {
        LazyColumn(
          modifier = modifier
            .fillMaxSize(),
          contentPadding = PaddingValues(bottom = lazyListBottomPadding)
        ) {
          itemsIndexed(
            items = listItem,
            key = { _, item -> item.musicId },
          ) { index, item ->
            MusicMediaItem(
              item = item,
              isFav = item.musicId.toString() in favoriteMusicMediaIdList(),
              currentMediaId = currentPlayerMediaId(),
              onItemClick = {
                onMusicClick(index, listItem)
              },
              isPlaying = { currentPlayerPlayingState() },
            )
          }
        }
      } else EmptyPage(message = "Nothing found")

    }

  }
}