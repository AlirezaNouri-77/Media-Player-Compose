package com.example.mediaplayerjetpackcompose.presentation.screen.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.data.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.VideoMediaItem

@Composable
fun VideoPage(
  navHostController: NavHostController,
  videoPageViewModel: VideoPageViewModel,
) {

  Scaffold(
    topBar = {
      Text(
        text = "Video",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(10.dp)
      )
    }
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(it),
    ) {

      if (videoPageViewModel.mediaStoreDataList.isNotEmpty()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
          items(
            items = videoPageViewModel.mediaStoreDataList,
            ) { videoMediaModel ->
            VideoMediaItem(
              item = videoMediaModel,
              imageBitmap = videoPageViewModel.getThumbnail(videoMediaModel.uri)!!,
              onItemClick = { item ->
                val videoUri = item.uri.toString().encodeStringNavigation()
                navHostController.navigate("PlayerScreen/${videoUri}") {
                  launchSingleTop = true
                }
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