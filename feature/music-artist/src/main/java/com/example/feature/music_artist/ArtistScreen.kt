package com.example.feature.music_artist

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
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.CategoryListItem
import com.example.core.designsystem.EmptyPage
import com.example.core.designsystem.Loading
import com.example.core.model.CategoryMusic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.ArtistRoute(
  modifier: Modifier = Modifier,
  artistViewModel: ArtistViewModel = koinViewModel<ArtistViewModel>(),
  animatedVisibilityScope: AnimatedVisibilityScope,
  navigateToCategory: (String) -> Unit,
) {

  var listItem = artistViewModel.artist.collectAsStateWithLifecycle()

  ArtistScreen(
    modifier = modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      renderInOverlayDuringTransition = false,
      exit = fadeOut(tween(150, 20)),
      enter = fadeIn(tween(150, 150, easing = LinearEasing)),
    ),
    listItems = listItem.value.toImmutableList(),
    animatedVisibilityScope = animatedVisibilityScope,
    isLoading = artistViewModel.isLoading,
    navigateToCategory = {
      navigateToCategory(it)
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArtistScreen(
  modifier: Modifier = Modifier.Companion,
  listItems: ImmutableList<CategoryMusic>,
  isLoading: Boolean,
  bottomLazyListPadding: Dp = LocalBottomPadding.current,
  animatedVisibilityScope: AnimatedVisibilityScope,
  navigateToCategory: (String) -> Unit,
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
        )
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
              .padding(innerPadding),
            contentPadding = PaddingValues(bottom = bottomLazyListPadding),
          ) {
            items(
              items = listItems,
              key = { it.categoryName.hashCode() }
            ) { item ->
              CategoryListItem(
                categoryName = item.categoryName,
                musicListSize = item.categoryList.size,
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