package com.shermanrex.feature.music_search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.shermanrex.core.designsystem.EmptyPage
import com.shermanrex.core.designsystem.LoadingComponent
import com.shermanrex.core.designsystem.music.MusicListComponent
import com.shermanrex.core.designsystem.util.DeviceSize
import com.shermanrex.core.designsystem.util.calculateWidthWindowSize
import com.shermanrex.core.model.MusicModel
import com.shermanrex.feature.music_search.component.SearchTextFieldComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.compose.koinViewModel

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchRoute(
    onMusicClick: (Int, List<MusicModel>) -> Unit,
) {
    val searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>()
    val uiState by searchViewModel.searchScreenUiState.collectAsStateWithLifecycle()

    SearchScreen(
        isLoading = uiState.isLoading,
        listItem = uiState.searchList.toImmutableList(),
        currentPlayerMediaId = uiState.playingMusicState.playingMusicInfo.musicID,
        isPlaying = uiState.playingMusicState.isPlaying,
        onMusicClick = { index, list -> onMusicClick(index, list) },
        searchTextFieldValue = uiState.searchTextFieldValue,
        onClearSearchTextField = { searchViewModel.onEvent(SearchScreenUiEvent.OnClearSearchTextField) },
        onSearchTextFieldValueChange = {
            searchViewModel.onEvent(SearchScreenUiEvent.OnSearchTextField(it))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    listItem: ImmutableList<MusicModel>,
    searchTextFieldValue: String,
    currentPlayerMediaId: String,
    isPlaying: Boolean,
    onMusicClick: (Int, List<MusicModel>) -> Unit,
    onSearchTextFieldValueChange: (String) -> Unit,
    onClearSearchTextField: () -> Unit,
) {
    val windowSize = calculateWidthWindowSize()
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            Column {
                AnimatedVisibility(!WindowInsets.isImeVisible || windowSize == DeviceSize.COMPACT) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Search",
                                modifier = modifier,
                                fontWeight = FontWeight.Bold,
                                fontSize = 38.sp,
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                    )
                }
                SearchTextFieldComponent(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    textFieldValue = searchTextFieldValue,
                    onTextFieldChange = onSearchTextFieldValueChange,
                    onClear = onClearSearchTextField,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) {
                LoadingComponent()
            } else {
                if (listItem.isNotEmpty()) {
                    MusicListComponent(
                        listItem = listItem,
                        isPlaying = isPlaying,
                        currentMusicId = currentPlayerMediaId,
                        onMusicClick = onMusicClick,
                    )
                } else if (searchTextFieldValue.isNotEmpty() && listItem.isEmpty()) {
                    EmptyPage(message = "Nothing found")
                } else {
                    EmptyPage()
                }
            }
        }
    }
}
