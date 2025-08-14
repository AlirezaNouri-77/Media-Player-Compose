package com.example.feature.music_artist

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
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.CategoryListItem
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.Loading
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.R
import com.example.core.designsystem.SortDropDownMenu
import com.example.core.model.MusicModel
import com.example.core.model.datastore.CategorizedSortType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.ArtistRoute(
  modifier: Modifier = Modifier,
  artistViewModel: ArtistViewModel = koinViewModel<ArtistViewModel>(),
  animatedVisibilityScope: AnimatedVisibilityScope,
  navigateToCategory: (String) -> Unit,
) {

  val uiState by artistViewModel.artistScreenUiState.collectAsStateWithLifecycle()

  ArtistScreen(
    modifier = modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      renderInOverlayDuringTransition = false,
      exit = fadeOut(tween(150, 20)),
      enter = fadeIn(tween(150, 150, easing = LinearEasing)),
    ),
    listItems = uiState.artistList,
    isLoading = uiState.isLoading,
    animatedVisibilityScope = animatedVisibilityScope,
    navigateToCategory = navigateToCategory,
    isDropDownMenuSortExpand = uiState.isSortDropDownMenuShow,
    isSortDescending = uiState.sortState.isDec,
    currentSortType = uiState.sortState.sortType,
    onSortIconClick = { artistViewModel.onEvent(ArtistUiEvent.ShowSortDropDownMenu) },
    onDismissDropDownMenu = { artistViewModel.onEvent(ArtistUiEvent.HideSortDropDownMenu) },
    onOrderClick = { artistViewModel.onEvent(ArtistUiEvent.UpdateSortOrder(!uiState.sortState.isDec)) },
    onSortClick = { artistViewModel.onEvent(ArtistUiEvent.UpdateSortType(it)) }
  )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArtistScreen(
  modifier: Modifier = Modifier,
  listItems: List<Pair<String, List<MusicModel>>>,
  isLoading: Boolean,
  isDropDownMenuSortExpand: Boolean,
  isSortDescending: Boolean,
  currentSortType: CategorizedSortType,
  animatedVisibilityScope: AnimatedVisibilityScope,
  navigateToCategory: (String) -> Unit,
  onSortIconClick: () -> Unit,
  onDismissDropDownMenu: () -> Unit,
  onOrderClick: () -> Unit,
  onSortClick: (CategorizedSortType) -> Unit,
) {

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Artist",
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
              .wrapContentSize(Alignment.TopEnd)
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
              isExpand = isDropDownMenuSortExpand,
              sortTypeList = CategorizedSortType.entries.toList(),
              isSortDescending = isSortDescending,
              currentSortType = currentSortType,
              onSortClick = { onSortClick(it as CategorizedSortType) },
              onOrderClick = { onOrderClick() },
              onDismiss = { onDismissDropDownMenu() },
            )
          }
        }
      )
    },
  ) { innerPadding ->

    Crossfade(isLoading) {
      if (it) {
        Loading(modifier = Modifier.fillMaxSize())
      } else {
        if (listItems.isNotEmpty()) {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .consumeWindowInsets(innerPadding),
            contentPadding = PaddingValues(bottom = LocalBottomPadding.current.calculateBottomPadding()),
          ) {
            items(
              items = listItems,
              key = { key -> key.first }
            ) { item ->
              CategoryListItem(
                categoryName = item.first,
                musicListSize = item.second.size,
                onClick = { categoryName ->
                  navigateToCategory(categoryName)
                },
                sharedTransitionScope = this@ArtistScreen,
                animatedVisibilityScope = animatedVisibilityScope,
              )
            }
          }

        } else EmptyPage()
      }
    }

  }
}