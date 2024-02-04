package com.example.mediaplayerjetpackcompose.presentation.screen.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.data.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.VideoMediaItem

@Composable
fun VideoPage(
  navHostController: NavHostController,
  paddingValues: PaddingValues,
  videoItemList: List<VideoMediaModel>,
) {

  Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(items = videoItemList, key = { item -> item.id }) {
        VideoMediaItem(
          item = it,
          onItemClick = { item ->
            val videoUri = item.uri.toString().encodeStringNavigation()
            navHostController.navigate("PlayerScreen/${videoUri}") {
              launchSingleTop = true
            }
          },
        )
      }
    }
  }

}