package com.shermanrex.feature.music_album

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shermanrex.core.designsystem.LoadingComponent
import com.shermanrex.core.designsystem.R
import com.shermanrex.core.designsystem.SortDropDownMenu
import com.shermanrex.core.designsystem.music.CategoryListComponent
import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.PlayingMusicInfo
import com.shermanrex.core.model.datastore.CategorizedSortType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.AlbumRoute(
    PlayingMusicInfo: PlayingMusicInfo,
    navigateToCategory: (String) -> Unit,
) {
    val albumArtistViewModel = koinViewModel<AlbumViewModel>()
    val uiState by albumArtistViewModel.albumScreenUiState.collectAsStateWithLifecycle()

    AlbumScreen(
        sharedTransitionScope = this,
        onEvent = albumArtistViewModel::onEvent,
        albumsList = uiState.albumList.toImmutableList(),
        navigateTo = navigateToCategory,
        isLoading = uiState.isLoading,
        isSortDescending = uiState.sortState.isDec,
        currentSortType = uiState.sortState.sortType,
        currentPlayerInfo = PlayingMusicInfo,
        isSortDropDownMenuExpanded = uiState.isSortDropDownMenuShow,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumScreen(
    modifier: Modifier = Modifier,
    onEvent: (AlbumUiEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    albumsList: ImmutableList<Pair<String, List<MusicModel>>>,
    isLoading: Boolean,
    currentPlayerInfo: PlayingMusicInfo,
    isSortDescending: Boolean,
    currentSortType: CategorizedSortType,
    navigateTo: (String) -> Unit,
    isSortDropDownMenuExpanded: Boolean,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Album",
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        fontSize = 38.sp,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd),
                    ) {
                        IconButton(
                            onClick = { onEvent(AlbumUiEvent.ShowSortDropDownMenu) },
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.icon_sort),
                                contentDescription = "Sort Icon",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                        SortDropDownMenu(
                            isExpand = isSortDropDownMenuExpanded,
                            sortTypeList = CategorizedSortType.entries.toList(),
                            isSortDescending = isSortDescending,
                            currentSortType = currentSortType,
                            onSortClick = { onEvent(AlbumUiEvent.UpdateSortType(it as CategorizedSortType)) },
                            onOrderClick = { onEvent(AlbumUiEvent.UpdateSortOrder(!isSortDescending)) },
                            onDismiss = { onEvent(AlbumUiEvent.HideSortDropDownMenu) },
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Crossfade(isLoading) {
            if (it) {
                LoadingComponent(modifier = Modifier.fillMaxSize())
            } else {
                CategoryListComponent(
                    modifier = Modifier.padding(innerPadding),
                    listItem = albumsList,
                    currentMusicId = currentPlayerInfo.musicID,
                    sharedTransitionScope = sharedTransitionScope,
                    navigateToCategoryPage = navigateTo,
                )
            }
        }
    }
}
