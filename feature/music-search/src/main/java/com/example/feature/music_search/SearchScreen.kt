package com.example.feature.music_search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.util.getLazyColumnPadding
import com.example.core.model.MusicModel
import com.example.feature.music_search.component.SearchTextFieldSection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.compose.koinViewModel

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchRoot(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    onMusicClick: (Int, List<MusicModel>) -> Unit,
) {
    val uiState by searchViewModel.searchScreenUiState.collectAsStateWithLifecycle()

    SearchScreen(
        modifier = modifier,
        listItem = uiState.searchList.toImmutableList(),
        currentPlayerMediaId = uiState.playerStateModel.currentMediaInfo.musicID,
        currentPlayerPlayingState = uiState.playerStateModel.isPlaying,
        onMusicClick = { index, list -> onMusicClick(index, list) },
        searchTextFieldValue = uiState.searchTextFieldValue,
        onClearSearchTextField = { searchViewModel.onEvent(SearchScreenUiEvent.OnClearSearchTextField) },
        onSearchTextFieldValueChange = {
            searchViewModel.onEvent(SearchScreenUiEvent.OnSearchTextField(it))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    listItem: ImmutableList<MusicModel>,
    searchTextFieldValue: String,
    currentPlayerMediaId: String,
    currentPlayerPlayingState: Boolean,
    onMusicClick: (Int, List<MusicModel>) -> Unit,
    onSearchTextFieldValueChange: (String) -> Unit,
    onClearSearchTextField: () -> Unit,
) {
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
                ),
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchTextFieldSection(
                textFieldValue = searchTextFieldValue,
                onTextFieldChange = onSearchTextFieldValueChange,
                onClear = onClearSearchTextField,
            )

            if (listItem.isNotEmpty()) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = getLazyColumnPadding(currentPlayerMediaId.isNotEmpty()),
                    ),
                ) {
                    itemsIndexed(
                        items = listItem,
                        key = { _, item -> item.musicId },
                    ) { index, item ->
                        MusicMediaItem(
                            musicId = item.musicId,
                            artworkUri = item.artworkUri,
                            name = item.name,
                            artist = item.artist,
                            duration = item.duration,
                            isFavorite = item.isFavorite,
                            currentMediaId = currentPlayerMediaId,
                            onItemClick = { onMusicClick(index, listItem) },
                            isPlaying = { currentPlayerPlayingState },
                        )
                    }
                }
            } else if (searchTextFieldValue.isNotEmpty()) {
                EmptyPage(message = "Nothing found")
            }
        }
    }
}
