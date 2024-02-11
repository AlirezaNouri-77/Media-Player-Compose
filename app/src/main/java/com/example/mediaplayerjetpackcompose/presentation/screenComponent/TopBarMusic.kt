package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.sortBar

@Composable
fun TopBarMusic(
  musicPageViewModel: MusicPageViewModel,
  showSortBar: Boolean,
  currentTabPosition: Int,
  onSortIconClick: () -> Unit,
  onTabBarClick: (Int) -> Unit,
) {
  Column {
    TabBar(
      musicPageViewModel = musicPageViewModel,
      currentTabPosition = currentTabPosition,
      onTabClick = { onTabBarClick.invoke(it) },
      onSortIconClick = { onSortIconClick.invoke() },
    )
    Spacer(modifier = Modifier.height(5.dp))
    AnimatedVisibility(
      visible = showSortBar,
      enter = fadeIn(tween(300, 100)) + slideInVertically(
        animationSpec = tween(300),
        initialOffsetY = { int -> int / 4 }),
      exit = slideOutVertically(
        animationSpec = tween(300, 100),
        targetOffsetY = { int -> int / 4 }) + fadeOut(tween(300, 100))
    ) {
      LazyRow(
        modifier = Modifier
          .padding(horizontal = 15.dp)
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {
        sortBar(
          musicPageViewModel = musicPageViewModel,
          onSortClick = {
            musicPageViewModel.currentListSort.value = it
            musicPageViewModel.sortMusicListByCategory(
              list = musicPageViewModel.musicList
            ).also { resultList ->
              musicPageViewModel.musicList =
                resultList as SnapshotStateList<MusicMediaModel>
            }
          },
          onDecClick = {
            musicPageViewModel.isDec.value = !musicPageViewModel.isDec.value
            musicPageViewModel.sortMusicListByCategory(
              list = musicPageViewModel.musicList
            ).also { resultList ->
              musicPageViewModel.musicList =
                resultList as SnapshotStateList<MusicMediaModel>
            }
          }
        )
      }
    }
  }
}