package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.core.designSystem.ThumbnailImage
import com.example.mediaplayerjetpackcompose.core.model.MediaPlayerState
import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import com.example.mediaplayerjetpackcompose.core.model.PlayerActions
import com.example.mediaplayerjetpackcompose.util.PagerHandler
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FullscreenPlayerPager(
  modifier: Modifier = Modifier,
  pagerItem: ImmutableList<MusicModel>,
  mediaPlayerState: MediaPlayerState,
  onPlayerAction: (action: PlayerActions) -> Unit,
  setCurrentPagerNumber: (Int) -> Unit,
  currentPagerPage: Int,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  val pagerState = rememberPagerState(
    initialPage = currentPagerPage,
    pageCount = { pagerItem.size },
  )

  PagerHandler(
    currentPlayerMediaId = mediaPlayerState.mediaId.toLong(),
    pagerMusicList = pagerItem,
    currentPagerPage = currentPagerPage,
    pagerState = pagerState,
    setCurrentPagerNumber = setCurrentPagerNumber,
    onMoveToIndex = { onPlayerAction(PlayerActions.OnMoveToIndex(it)) },
  )

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    HorizontalPager(
      modifier = Modifier
        .then(
          if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Modifier
              .fillMaxWidth()
              .height(360.dp)
          } else {
            Modifier.size(250.dp)
          }
        ),
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
        uri = pagerItem[page].artworkUri,
      )
    }
  }
}
