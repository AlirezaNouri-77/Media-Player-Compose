package com.example.mediaplayerjetpackcompose.presentation.screen.video

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MainScreenNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.repository.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoItemModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.EmptyPage
import com.example.mediaplayerjetpackcompose.presentation.screen.component.Loading
import com.example.mediaplayerjetpackcompose.presentation.screen.video.component.TopBarVideo
import com.example.mediaplayerjetpackcompose.presentation.screen.video.item.VideoMediaItem

@Composable
fun VideoPage(
  navHostController: NavHostController,
  videoPageViewModel: VideoPageViewModel,
  onNavigateToMusicScreen: () -> Unit,
  uiState: () -> State<MediaStoreResult<VideoItemModel>>,
) {

  Scaffold(
    topBar = {
      TopBarVideo(
        onBackClick = {
          onNavigateToMusicScreen()
        },
      )
    }
  ) { innerPadding ->

    AnimatedContent(
      targetState = uiState().value,
      modifier = Modifier.padding(innerPadding),
      transitionSpec = { fadeIn().togetherWith(fadeOut()) },
      label = ""
    ) {

      when (it) {
        MediaStoreResult.Loading -> Loading(
          modifier = Modifier
            .fillMaxSize(),
        )

        MediaStoreResult.Empty -> EmptyPage()

        is MediaStoreResult.Result -> Box(
          modifier = Modifier
            .fillMaxSize(),
        ) {
          if (videoPageViewModel.mediaStoreDataList.isNotEmpty()) {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              contentPadding = PaddingValues(top = 4.dp)
            ) {
              itemsIndexed(
                items = videoPageViewModel.mediaStoreDataList,
                key = { index, _ -> videoPageViewModel.mediaStoreDataList[index].videoId },
              ) { index, videoMediaModel ->

                VideoMediaItem(
                  item = videoMediaModel,
                  onItemClick = {
                    navHostController.navigate(MainScreenNavigationModel.VideoPlayerScreen("")) {
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

        else -> {}
      }
    }
  }
}