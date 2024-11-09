package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.PagerThumbnailModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.ThumbnailImage

@Composable
fun FullscreenPlayerPager(
  modifier: Modifier = Modifier,
  pagerItem: SnapshotStateList<PagerThumbnailModel>,
  pagerState: PagerState,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  val pagerModifier = remember(orientation) {
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      Modifier
        .fillMaxWidth()
        .height(380.dp)
    } else {
      Modifier.size(250.dp)
    }
  }

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    HorizontalPager(
      modifier = pagerModifier,
      beyondViewportPageCount = 1,
      state = pagerState,
      pageSpacing = 10.dp,
      verticalAlignment = Alignment.CenterVertically,
    ) { page ->
      ThumbnailImage(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp, vertical = 10.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(color = MaterialTheme.colorScheme.primary),
        uri = pagerItem[page].uri,
      )
    }
  }
}
