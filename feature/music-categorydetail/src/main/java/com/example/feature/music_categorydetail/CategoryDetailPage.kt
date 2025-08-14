package com.example.feature.music_categorydetail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.MusicMediaItem
import com.example.core.model.MusicModel
import com.example.core.model.navigation.ParentCategoryRoute
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailRoute(
  categoryViewModel: CategoryViewModel,
  categoryName: String,
  currentMusicId: String,
  isPlayerPlaying: () -> Boolean,
  onMusicClick: (index: Int, list: List<MusicModel>) -> Unit,
  onBackClick: () -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
) {

  val uiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

  CategoryDetailPage(
    modifier = Modifier.sharedBounds(
      sharedContentState = rememberSharedContentState("bound"),
      animatedVisibilityScope = animatedVisibilityScope,
      renderInOverlayDuringTransition = false,
      exit = fadeOut(tween(150, 20)),
      enter = fadeIn(tween(150, 150, easing = LinearEasing)),
    ),
    categoryName = categoryName,
    listItem = uiState.songList.toImmutableList(),
    lazyListBottomPadding = LocalBottomPadding.current.calculateBottomPadding(),
    currentMusicId = currentMusicId,
    isPlayerPlaying = { isPlayerPlaying() },
    onMusicClick = { onMusicClick(it, uiState.songList) },
    onBackClick = onBackClick,
    animatedVisibilityScope = animatedVisibilityScope,
  )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailPage(
  modifier: Modifier = Modifier,
  categoryName: String,
  listItem: ImmutableList<MusicModel>,
  currentMusicId: String,
  isPlayerPlaying: () -> Boolean,
  onMusicClick: (index: Int) -> Unit,
  lazyListBottomPadding: Dp,
  onBackClick: () -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(
            modifier = Modifier
              .sharedElement(
                sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                animatedVisibilityScope = animatedVisibilityScope,
              )
              .basicMarquee(),
            text = categoryName,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
          )
        },
        navigationIcon = {
          IconButton(onClick = { onBackClick.invoke() }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
              contentDescription = "",
              modifier = Modifier
                .size(35.dp),
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          titleContentColor = MaterialTheme.colorScheme.onPrimary,
        )
      )
    },
  ) { innerPadding ->

    LazyColumn(
      Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(top = 10.dp, bottom = lazyListBottomPadding)
    ) {
      itemsIndexed(
        items = listItem,
        key = { _, item -> item.musicId },
      ) { index, item ->
        MusicMediaItem(
          item = item,
          currentMediaId = currentMusicId,
          onItemClick = {
            onMusicClick.invoke(index)
          },
          isPlaying = { isPlayerPlaying() },
        )
      }
    }

  }
}