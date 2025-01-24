package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun MiniMusicPlayer(
  modifier: Modifier,
  onClick: () -> Unit,
  artworkPagerList: List<MusicModel>,
  setCurrentPagerNumber: (Int) -> Unit,
  currentPagerPage: Int,
  currentPlayerMediaId: Long,
  currentPlayerDuration: Int,
  currentPlayerArtworkUri: Uri?,
  isPlayerPlaying: Boolean,
  currentMusicPosition: () -> Long,
  onPlayerAction: (action: PlayerActions) -> Unit,
) {

  val pagerState = rememberPagerState(
    initialPage = currentPagerPage,
    pageCount = { artworkPagerList.size },
  )

  PagerHandler(
    currentPlayerMediaId = currentPlayerMediaId,
    pagerMusicList = { artworkPagerList },
    currentPagerPage = { currentPagerPage },
    pagerState = pagerState,
    setCurrentPagerNumber = setCurrentPagerNumber,
    onMoveToIndex = { onPlayerAction(PlayerActions.OnMoveToIndex(it)) },
  )

  val marqueeAnimate = remember(pagerState.isScrollInProgress) {
    if (pagerState.isScrollInProgress) 0 else Int.MAX_VALUE
  }

  Card(
    modifier = modifier,
    shape = RoundedCornerShape(0.dp),
    onClick = { onClick.invoke() },
    colors = CardDefaults.cardColors(
      containerColor = Color.Transparent,
    ),
    interactionSource = NoRippleEffect,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp, horizontal = 10.dp)
          .drawWithContent {
            drawContent()

            // margin times by 2 because i applied 15.dp to end and start
            var margin = 15.dp.toPx()

            val progress = (currentMusicPosition() * (this.size.width - margin.times(2))) / currentPlayerDuration

            drawLine(
              color = Color.White,
              alpha = 0.4f,
              strokeWidth = 2.dp.toPx(),
              start = Offset(x = margin, y = this.size.height + 6.dp.toPx()),
              end = Offset(x = this.size.width - margin, y = this.size.height + 6.dp.toPx())
            )
            drawLine(
              color = Color.White,
              strokeWidth = 2.dp.toPx(),
              start = Offset(x = 15.dp.toPx(), y = this.size.height + 6.dp.toPx()),
              end = Offset(x = progress + margin, y = this.size.height + 6.dp.toPx())
            )

          },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        ThumbnailImage(
          modifier = Modifier
            .weight(0.2f, false)
            .size(45.dp)
            .clip(RoundedCornerShape(5.dp)),
          uri = currentPlayerArtworkUri,
        )
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .weight(0.7f),
          verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
          horizontalAlignment = Alignment.Start,
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(),
            contentAlignment = Alignment.Center,
          ) {
            HorizontalPager(
              modifier = Modifier.fillMaxWidth(),
              state = pagerState,
              pageSpacing = 20.dp,
              beyondViewportPageCount = 2,
              contentPadding = PaddingValues(horizontal = 2.dp)
            ) { page ->
              Column(
                modifier = Modifier
                  .fillMaxWidth(0.95f),
              ) {
                Text(
                  modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(iterations = marqueeAnimate, initialDelayMillis = 500),
                  text = artworkPagerList[page].name.removeFileExtension(),
                  fontSize = 13.sp,
                  maxLines = 1,
                  color = Color.White,
                )
                Text(
                  modifier = Modifier
                    .fillMaxWidth(),
                  text = artworkPagerList[page].artist,
                  fontSize = 12.sp,
                  maxLines = 1,
                  color = Color.White,
                )
              }
            }
          }
        }
        IconButton(
          modifier = Modifier
            .weight(0.1f)
            .padding(7.dp)
            .size(18.dp),
          onClick = {
            when (isPlayerPlaying) {
              true -> onPlayerAction(PlayerActions.PausePlayer)
              false -> onPlayerAction(PlayerActions.ResumePlayer)
            }
          },
          interactionSource = NoRippleEffect,
        ) {
          Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = if (isPlayerPlaying) R.drawable.icon_pause else R.drawable.icon_play),
            contentDescription = "",
            tint = Color.White,
          )
        }
      }
    }
  }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
  MediaPlayerJetpackComposeTheme {
    MiniMusicPlayer(
      onClick = {},
      artworkPagerList = listOf(MusicModel.Empty),
      setCurrentPagerNumber = {},
      currentPagerPage = 0,
      currentMusicPosition = { 2000 },
      onPlayerAction = {},
      modifier = Modifier.height(70.dp).padding(horizontal = 8.dp, vertical = 5.dp),
      currentPlayerMediaId = 0L,
      currentPlayerDuration = 207726,
      currentPlayerArtworkUri = Uri.EMPTY,
      isPlayerPlaying = true,
    )
  }
}