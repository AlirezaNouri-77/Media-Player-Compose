package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.animation.AnimatedVisibilityScope
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
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.ArtistPage(
  modifier: Modifier = Modifier,
  listItems: List<CategoryMusicModel>,
  bottomLazyListPadding: Dp,
  animatedVisibilityScope: AnimatedVisibilityScope,
  navigateTo: (String) -> Unit,
) {

  Scaffold(
    modifier = modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      renderInOverlayDuringTransition = false,
      exit = fadeOut(tween(150, 20)),
      enter = fadeIn(tween(150, 150, easing = LinearEasing)),
    ),
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
              navigateTo(categoryName)
            },
            sharedTransitionScope = this@ArtistPage,
            animatedVisibilityScope = animatedVisibilityScope,
          )
        }
      }

    } else EmptyPage()

  }

}