package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.MediaCurrentState
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.component.verticalFadeEdge

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MiniMusicPlayer(
  modifier: Modifier,
  pagerMusicList: List<MusicModel>,
  setCurrentPagerNumber: (Int) -> Unit,
  currentPagerPage: Int,
  currentMediaState: () -> MediaCurrentState,
  currentMusicPosition: () -> Long,
  onClick: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
  onMoveNextMusic: () -> Unit,
  onMovePreviousMusic: (Boolean) -> Unit,
) {

  val pagerState = rememberPagerState(
    initialPage = currentPagerPage,
    pageCount = { pagerMusicList.size },
  )

  PagerHandler(
    currentMediaState = currentMediaState,
    pagerMusicList = pagerMusicList,
    currentPagerPage = currentPagerPage,
    pagerState = pagerState,
    setCurrentPagerNumber = setCurrentPagerNumber,
    onMoveNextMusic = onMoveNextMusic,
    onMovePreviousMusic = onMovePreviousMusic,
  )

  val reactCanvasColor = MaterialTheme.colorScheme.onPrimary
  val duration = remember(currentMediaState().metaData) {
    currentMediaState().metaData.extras?.getInt("Duration") ?: 0
  }
  val playAndPauseIcon = remember(currentMediaState().isPlaying) {
    if (currentMediaState().isPlaying) R.drawable.icon_pause_24 else R.drawable.icon_play_arrow_24
  }
  val marqueeAnimate = remember(pagerState.isScrollInProgress) {
    if (pagerState.isScrollInProgress) 0 else Int.MAX_VALUE
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = 10.dp),
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

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceAround,
      modifier = Modifier
        .navigationBarsPadding()
        .fillMaxWidth()
        .padding(horizontal = 6.dp, vertical = 5.dp),
    ) {
      ArtworkImage(
        modifier = Modifier
          .weight(0.2f, false)
          .size(45.dp)
          .clip(RoundedCornerShape(5.dp)),
        inset = 30f,
        uri = { currentMediaState().metaData.artworkUri },
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
            beyondBoundsPageCount = 2,
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
                  .verticalFadeEdge()
                  .basicMarquee(iterations = marqueeAnimate),
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
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .height(4.dp)
            .graphicsLayer {
              shape = RoundedCornerShape(4.dp)
              clip = true
            }
            .drawBehind {
              val size = this.size.width
              val progress = (currentMusicPosition() * size) / duration
              drawRoundRect(color = reactCanvasColor.copy(alpha = 0.1f))
              clipRect(
                right = progress,
              ) {
                this.drawRect(color = reactCanvasColor)
              }
            },
        )
      }

      Column(
        modifier = Modifier
          .weight(0.1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        IconButton(
          modifier = Modifier
            .size(50.dp),
          onClick = {
            when (currentMediaState().isPlaying) {
              true -> onPauseMusic.invoke()
              false -> onResumeMusic.invoke()
            }
          },
        ) {
          Icon(
            painter = painterResource(id = playAndPauseIcon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimary,
          )
        }
        Text(
          modifier = Modifier,
          text = currentMusicPosition().toInt().convertMilliSecondToTime(),
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onPrimary,
          fontWeight = FontWeight.Medium,
        )
      }

    }
  }
}
