package com.example.feature.music_album

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.repository.albumName
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.CategoryListItem
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.Loading
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.AlbumRoute(
  modifier: Modifier = Modifier,
  albumViewModel: AlbumViewModel = koinViewModel<AlbumViewModel>(),
  navigateToCategory: (String) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  bottomPadding: Dp = LocalBottomPadding.current,
) {

  var albumListData = albumViewModel.album.collectAsStateWithLifecycle()

  AlbumScreen(
    modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      renderInOverlayDuringTransition = false,
      exit = fadeOut(tween(150, 20)),
      enter = fadeIn(tween(150, 150, easing = LinearEasing)),
    ),
    albumListData = albumListData.value.toImmutableMap(),
    lazyListBottomPadding = bottomPadding,
    navigateTo = {
      navigateToCategory(it)
    },
    isLoading = albumViewModel.isLoading,
    animatedVisibilityScope = animatedVisibilityScope,
    sharedTransitionScope = this,
  )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun AlbumScreen(
  modifier: Modifier = Modifier,
  albumListData: ImmutableMap<albumName, List<MusicModel>>,
  lazyListBottomPadding: Dp,
  isLoading: Boolean,
  navigateTo: (String) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
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
        )
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
            contentPadding = PaddingValues(bottom = lazyListBottomPadding),
          ) {
            items(
              items = albumListData.keys.toList(),
              key = { it }
            ) { item ->
              CategoryListItem(
                categoryName = item,
                musicListSize = albumListData.getValue(item).size,
                onClick = { categoryName ->
                  navigateTo(categoryName)
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
              )
            }
          }

        } else EmptyPage()
      }
    }


  }
}