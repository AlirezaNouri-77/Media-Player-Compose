package com.example.feature.music_categorydetail

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.common.util.convertMilliSecondToTime
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.MusicThumbnail
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailRoute(
  categoryViewModel: CategoryViewModel,
  categoryName: String,
  currentMusicId: String,
  isPlayerPlaying: Boolean,
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
    isPlayerPlaying = isPlayerPlaying,
    onMusicClick = { onMusicClick(it, uiState.songList) },
    onBackClick = onBackClick,
    animatedVisibilityScope = animatedVisibilityScope,
    gradientColor = uiState.gradientColor,
  )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailPage(
  modifier: Modifier = Modifier,
  categoryName: String,
  gradientColor: Int,
  listItem: ImmutableList<MusicModel>,
  currentMusicId: String,
  isPlayerPlaying: Boolean,
  onMusicClick: (index: Int) -> Unit,
  lazyListBottomPadding: Dp,
  onBackClick: () -> Unit,
  showGridThumbnail: Boolean = false,
  animatedVisibilityScope: AnimatedVisibilityScope,
) {
  val animatedColor by animateColorAsState(
    targetValue = Color(gradientColor),
    animationSpec = tween(durationMillis = 100)
  )
  var currentImageSize by remember {
    mutableStateOf(240.dp)
  }

  val connection = remember {
    object : NestedScrollConnection {
      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y.toInt()
        val newSize = currentImageSize.value + delta
        val pre = currentImageSize.value
        currentImageSize = newSize.coerceIn(80f, 240f).dp
        return Offset(x = 0f, y = currentImageSize.value - pre)
      }
    }
  }

  Scaffold(
    modifier = modifier
      .drawWithCache {
        onDrawWithContent {
          drawRect(
            brush = Brush.verticalGradient(
              0.4f to animatedColor.copy(alpha = 0.8f),
              0.7f to animatedColor.copy(alpha = 0.7f),
              1f to Color.Transparent,
              endY = this.size.height,
            ),
          )
          drawContent()
        }
      },
    topBar = {
      TopAppBar(
        title = {},
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
    containerColor = Color.Transparent,
  ) { innerPadding ->

    if (listItem.isNotEmpty()) {
      Column(
        Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .nestedScroll(connection),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        if (showGridThumbnail) {
          LazyVerticalGrid(
            modifier = Modifier.size(240.dp),
            columns = GridCells.Fixed(2),
            userScrollEnabled = false,
          ) {
            items(4) {
              MusicThumbnail(
                uri = listItem.map { it.musicId to it.artworkUri }.toSet().random().second.toUri(),
                modifier = Modifier
                  .aspectRatio(1f)
                  .size(currentImageSize)
                  .clip(RoundedCornerShape(5.dp)),
              )
            }
          }
        } else {
          MusicThumbnail(
            uri = listItem.firstOrNull { it.artworkUri.isNotEmpty() }?.artworkUri?.toUri() ?: Uri.EMPTY,
            modifier = Modifier
              .size(currentImageSize)
              .clip(RoundedCornerShape(5.dp)),
          )
        }


        Text(
          modifier = Modifier
            .sharedElement(
              sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
              animatedVisibilityScope = animatedVisibilityScope,
            )
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
            .basicMarquee(),
          text = categoryName,
          fontSize = 22.sp,
          fontWeight = FontWeight.SemiBold,
        )
        Text(
          modifier = Modifier.padding(vertical = 4.dp),
          text = "${listItem.size}  music, ${
            listItem.map { it.duration }.reduce { acc, duration -> duration + acc }.convertMilliSecondToTime()
          }"
        )
        LazyColumn(
          contentPadding = PaddingValues(top = 10.dp, bottom = LocalBottomPadding.current.calculateBottomPadding() + 70.dp)
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
              isPlaying = isPlayerPlaying,
            )
          }
        }
      }
    }

  }
}

@Composable
fun CategoryDetailTopbar(
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {

  }
}

@Preview
@Composable
private fun CategoryDetailTopbarPreview() {
  MediaPlayerJetpackComposeTheme {
    CategoryDetailTopbar(

    )
  }
}