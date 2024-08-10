package com.example.mediaplayerjetpackcompose.presentation.screen.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.component.LoadingPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.item.VideoMediaItem

@Composable
fun VideoPage(
  navHostController: NavHostController,
  videoPageViewModel: VideoPageViewModel,
) {

  Scaffold(
    contentWindowInsets = WindowInsets(0.dp),
    topBar = {
      Text(
        text = "Videos",
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.primaryContainer)
          .padding(10.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
      )
    }
  ) {

    if (videoPageViewModel.isLoading) {
      LoadingPage(
        modifier = Modifier
          .fillMaxSize()
          .padding(it),
      )
    } else {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(it),
      ) {
        if (videoPageViewModel.mediaStoreDataList.isNotEmpty()) {
          LazyColumn(
            modifier = Modifier.fillMaxSize()
          ) {
            itemsIndexed(
              items = videoPageViewModel.mediaStoreDataList,
              key = { _, item -> item.videoId },
            ) { index, videoMediaModel ->

              VideoMediaItem(
                item = videoMediaModel,
                onItemClick = {
                  navHostController.navigate("PlayerScreen") {
                    launchSingleTop = true
                  }
                  videoPageViewModel.startPlay(index, videoPageViewModel.mediaStoreDataList)
                },
              )

            }
          }
        } else {
          EmptyPage()
        }
      }
    }

  }
}