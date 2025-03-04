package com.example.feature.music_player.fullScreen.component

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
import androidx.core.net.toUri
import com.example.core.music_media3.PlayerStateModel
import com.example.core.designsystem.MusicThumbnail
import com.example.feature.music_player.PagerHandler
import com.example.feature.music_player.PlayerActions
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FullscreenPlayerPager(
  modifier: Modifier = Modifier,
  pagerItem: ImmutableList<MusicModel>,
  playerStateModel: PlayerStateModel,
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
    currentPlayerMediaId = playerStateModel.currentMediaInfo.musicID.toLong(),
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
      MusicThumbnail(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp, vertical = 10.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(color = MaterialTheme.colorScheme.primary),
        uri = pagerItem[page].artworkUri.toUri(),
      )
    }
  }
}
