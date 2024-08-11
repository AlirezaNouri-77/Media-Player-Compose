package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler

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
      .fillMaxWidth(),
    onClick = { onClick.invoke() },
    shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
    ),
    interactionSource = NoRippleEffect,
  ) {

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceAround,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 6.dp, vertical = 5.dp),
    ) {
      ArtworkImage(
        modifier = Modifier
          .weight(0.2f, false)
          .size(45.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(color = MaterialTheme.colorScheme.primary),
        inset = 30f,
        uri = { currentMediaState().metaData.artworkUri },
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.6f),
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                  .fillMaxWidth()
                  .basicMarquee(iterations = marqueeAnimate),
                maxLines = 1,
              )
              Text(
                text = pagerMusicList[page].artist,
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
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
            .height(4.dp)
            .graphicsLayer {
              shape = RoundedCornerShape(4.dp)
              clip = true
            }
            .drawBehind {
              val size = this.size.width
              val progress = (currentMusicPosition() * size) / duration
              clipRect(
                right = progress,
              ) {
                this.drawRect(color = reactCanvasColor)
              }
            },
        )
      }

      IconButton(
        modifier = Modifier
          .weight(0.2f, false)
          .size(40.dp),
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

    }
  }
}
