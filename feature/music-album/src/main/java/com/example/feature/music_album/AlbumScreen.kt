package com.example.feature.music_album

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.core.designsystem.CategoryListItem
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.Loading
import com.example.core.designsystem.LocalParentScaffoldPadding
import com.example.core.designsystem.R
import com.example.core.designsystem.SortDropDownMenu
import com.example.core.model.MusicModel
import com.example.core.model.datastore.CategorizedSortType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.AlbumRoute(
    modifier: Modifier = Modifier,
    albumViewModel: AlbumViewModel = koinViewModel<AlbumViewModel>(),
    navigateToCategory: (String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val uiState by albumViewModel.albumScreenUiState.collectAsStateWithLifecycle()

    AlbumScreen(
        modifier.sharedBounds(
            sharedContentState = rememberSharedContentState("bound"),
            animatedVisibilityScope = animatedVisibilityScope,
            renderInOverlayDuringTransition = false,
            exit = fadeOut(tween(150, 20)),
            enter = fadeIn(tween(150, 150, easing = LinearEasing)),
        ),
        sharedTransitionScope = this,
        animatedVisibilityScope = animatedVisibilityScope,
        albumListData = uiState.albumList.toImmutableList(),
        navigateTo = navigateToCategory,
        isLoading = uiState.isLoading,
        isSortDescending = uiState.sortState.isDec,
        currentSortType = uiState.sortState.sortType,
        isSortDropDownMenuExpanded = uiState.isSortDropDownMenuShow,
        onSortIconClick = { albumViewModel.onEvent(AlbumUiEvent.ShowSortDropDownMenu) },
        onDismissDropDownMenu = { albumViewModel.onEvent(AlbumUiEvent.HideSortDropDownMenu) },
        onOrderClick = { albumViewModel.onEvent(AlbumUiEvent.UpdateSortOrder(!uiState.sortState.isDec)) },
        onSortClick = { albumViewModel.onEvent(AlbumUiEvent.UpdateSortType(it)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumScreen(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    albumListData: ImmutableList<Pair<String, List<MusicModel>>>,
    isLoading: Boolean,
    isSortDescending: Boolean,
    currentSortType: CategorizedSortType,
    navigateTo: (String) -> Unit,
    isSortDropDownMenuExpanded: Boolean,
    onDismissDropDownMenu: () -> Unit,
    onOrderClick: () -> Unit,
    onSortIconClick: () -> Unit,
    onSortClick: (CategorizedSortType) -> Unit,
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
                            onClick = { onSortIconClick.invoke() },
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
                            onSortClick = { onSortClick(it as CategorizedSortType) },
                            onOrderClick = { onOrderClick() },
                            onDismiss = { onDismissDropDownMenu() },
                        )
                    }
                },
            )
        },
    ) { innerPadding ->

        Crossfade(isLoading) {
            if (it) {
                Loading(modifier = Modifier.fillMaxSize())
            } else {
                if (albumListData.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(bottom = LocalParentScaffoldPadding.current.calculateBottomPadding()),
                    ) {
                        items(
                            items = albumListData,
                            key = { it },
                        ) { item ->
                            CategoryListItem(
                                categoryName = item.first,
                                musicListSize = item.second.size,
                                onClick = { categoryName ->
                                    navigateTo(categoryName)
                                },
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
                        }
                    }
                } else {
                    EmptyPage()
                }
            }
        }
    }
}
