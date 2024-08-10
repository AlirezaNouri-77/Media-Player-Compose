package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.ArtworkImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerArtwork(
  modifier: Modifier = Modifier,
  musicList: List<MusicModel>,
  pagerState: PagerState,
) {

  Box(
    modifier = modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center,
  ) {
    HorizontalPager(
      modifier = Modifier
        .fillMaxWidth()
        .height(340.dp),
      beyondBoundsPageCount = 1,
      state = pagerState,
      pageSpacing = 10.dp,
      verticalAlignment = Alignment.CenterVertically,
    ) { page ->
      ArtworkImage(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp, vertical = 10.dp)
          .clip(RoundedCornerShape(15.dp))
          .background(color = MaterialTheme.colorScheme.primary),
        uri = { musicList[page].artworkUri },
        inset = 250f,
      )
    }
  }
}
