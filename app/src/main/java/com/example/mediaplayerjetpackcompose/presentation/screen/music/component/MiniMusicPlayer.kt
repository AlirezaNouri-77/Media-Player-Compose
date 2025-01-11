package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.PagerThumbnailModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun MiniMusicPlayer(
  modifier: Modifier,
  onClick: () -> Unit,
  pagerMusicList: List<PagerThumbnailModel>,
  setCurrentPagerNumber: (Int) -> Unit,
  currentPagerPage: Int,
  mediaPlayerState: () -> MediaPlayerState,
  currentMusicPosition: () -> Long,
  onPlayerAction: (action: PlayerActions) -> Unit,
) {

  val pagerState = rememberPagerState(
    initialPage = currentPagerPage,
    pageCount = { pagerMusicList.size },
  )

  PagerHandler(
    currentPlayerMediaId = currentPlayerMediaId,
    pagerMusicList = { pagerMusicList },
    currentPagerPage = { currentPagerPage },
    pagerState = pagerState,
    setCurrentPagerNumber = setCurrentPagerNumber,
    onMoveToIndex = { onPlayerAction(PlayerActions.OnMoveToIndex(it)) },
  )

  val reactCanvasColor = MaterialTheme.colorScheme.onPrimary

  val marqueeAnimate = remember(pagerState.isScrollInProgress) {
    if (pagerState.isScrollInProgress) 0 else Int.MAX_VALUE
  }

  Card(
    modifier = modifier
      .fillMaxWidth(),
    onClick = { onClick.invoke() },
    shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
    ),
    elevation = CardDefaults.elevatedCardElevation(
      defaultElevation = 10.dp,
    ),
    interactionSource = NoRippleEffect,
  ) {

    Column(
      modifier = Modifier
        .navigationBarsPadding()
        .fillMaxWidth()
        .padding(horizontal = 6.dp, vertical = 5.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
      ) {
        ThumbnailImage(
          modifier = Modifier
            .weight(0.2f, false)
            .size(55.dp)
            .clip(RoundedCornerShape(5.dp)),
          uri = mediaPlayerState().metaData.artworkUri,
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
              contentPadding = PaddingValues(horizontal = 10.dp)
            ) { page ->
              Column(
                modifier = Modifier
                  .fillMaxWidth(0.95f),
              ) {
                Text(
                  text = pagerMusicList[page].name.removeFileExtension(),
                  fontSize = 13.sp,
                  modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(iterations = marqueeAnimate, initialDelayMillis = 500),
                  maxLines = 1,
                )
                Text(
                  text = pagerMusicList[page].artist,
                  fontSize = 12.sp,
                  modifier = Modifier
                    .fillMaxWidth(),
                  maxLines = 1,
                )
              }
            }
          }
        }
        IconButton(
          modifier = Modifier
            .weight(0.1f)
            .padding(7.dp),
          onClick = {
            when (mediaPlayerState().isPlaying) {
              true -> onPlayerAction(PlayerActions.PausePlayer)
              false -> onPlayerAction(PlayerActions.ResumePlayer)
            }
          },
          interactionSource = NoRippleEffect,
        ) {
          Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(id = if (mediaPlayerState().isPlaying) R.drawable.icon_pause else R.drawable.icon_play),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimary,
          )
        }
      }
      Row(
        modifier = Modifier
          .fillMaxWidth(0.7f)
          .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
      ) {
        Text(
          modifier = Modifier,
          text = currentMusicPosition().convertMilliSecondToTime(),
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onPrimary,
          fontWeight = FontWeight.Medium,
        )
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(0.8f)
            .height(4.dp)
            .graphicsLayer {
              shape = RoundedCornerShape(4.dp)
              clip = true
            }
            .drawBehind {
              val size = this.size.width
              val progress = (currentMusicPosition() * size) / (mediaPlayerState().metaData.extras?.getInt(Constant.DURATION_KEY) ?: 0)
              drawRoundRect(color = reactCanvasColor.copy(alpha = 0.1f))
              clipRect(
                right = progress,
              ) {
                this.drawRect(color = reactCanvasColor)
              }
            },
        )
        Text(
          modifier = Modifier,
          text = mediaPlayerState().metaData.extras?.getInt(Constant.DURATION_KEY).convertMilliSecondToTime(),
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onPrimary,
          fontWeight = FontWeight.Medium,
        )
      }
    }

  }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
  var list = listOf<PagerThumbnailModel>(
    PagerThumbnailModel(
      uri = Uri.EMPTY,
      musicId = 0,
      name = "ExampleName.mp3",
      artist = "Example Artist",
    ),
  )
  MediaPlayerJetpackComposeTheme {
    MiniMusicPlayer(
      onClick = {},
      pagerMusicList = list,
      setCurrentPagerNumber = {},
      currentPagerPage = 0,
      mediaPlayerState = { MediaPlayerState.Empty },
      currentMusicPosition = { 50_000L },
      onPlayerAction = {},
      modifier = Modifier,
    )
  }
}