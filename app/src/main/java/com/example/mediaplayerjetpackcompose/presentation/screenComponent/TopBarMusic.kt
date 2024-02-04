package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.sortBar

@Composable
fun TopBarMusic(
  musicPageViewModel: MusicPageViewModel,
  showSortBar: Boolean,
  onSortIconClick: () -> Unit,
) {
  Column(Modifier.animateContentSize()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
      Text(
        text = "Music",
        modifier = Modifier
          .padding(10.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
      )
      Image(
        painter = painterResource(id = R.drawable.icon_sort),
        contentDescription = "Sort Icon",
        modifier = Modifier
          .padding(10.dp)
          .size(30.dp)
          .clickable {
            onSortIconClick.invoke()
            // showSortBar = !showSortBar
          },
      )
    }
    TabBar(musicPageViewModel = musicPageViewModel)
    Spacer(modifier = Modifier.height(5.dp))
    AnimatedVisibility(
      visible = showSortBar,
      enter = fadeIn() + slideInVertically(
        animationSpec = tween(300),
        initialOffsetY = { int -> -int / 2 }),
      exit = slideOutVertically(
        animationSpec = tween(300),
        targetOffsetY = { int -> -int / 2 }) + fadeOut(),
    ) {
      LazyRow(modifier = Modifier.padding(horizontal = 15.dp)) {
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
          onDec = {
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