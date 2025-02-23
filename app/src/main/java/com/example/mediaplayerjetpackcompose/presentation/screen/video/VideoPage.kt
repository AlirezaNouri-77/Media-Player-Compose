package com.example.mediaplayerjetpackcompose.presentation.screen.video

import android.app.Activity
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.util.Constant
import com.example.mediaplayerjetpackcompose.core.designSystem.Loading
import com.example.mediaplayerjetpackcompose.navigation.MainNavRoute
import com.example.mediaplayerjetpackcompose.core.model.MediaStoreResult
import com.example.mediaplayerjetpackcompose.util.isPermissionDenied
import com.example.mediaplayerjetpackcompose.util.openSetting
import com.example.mediaplayerjetpackcompose.util.shouldShowPermissionRationale
import com.example.mediaplayerjetpackcompose.presentation.screen.video.component.EmptyVideoResultHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.video.component.TopBarVideo
import com.example.mediaplayerjetpackcompose.presentation.screen.video.item.VideoMediaItem

@Composable
fun VideoPage(
  navHostController: NavHostController,
  videoPageViewModel: VideoPageViewModel,
  onNavigateToMusicScreen: () -> Unit,
  context: Context = LocalContext.current,
  activity: Activity? = LocalActivity.current,
) {

  val videoUiState = videoPageViewModel.uiState.collectAsStateWithLifecycle()

  val onRefreshVideoList: () -> Unit = { videoPageViewModel.getVideo() }

  var activityResultApi34 = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
    onRefreshVideoList()
  }

  var activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    onRefreshVideoList()
  }

  Scaffold(
    topBar = {
      TopBarVideo(
        context = context,
        onBackClick = {
          onNavigateToMusicScreen()
        },
        onSelectVideo = {
          var isPermissionsGrant = Constant.videoPermission.all { context.isPermissionDenied(it) }

          if (isPermissionsGrant) {
            if (Constant.videoPermission.all { context.shouldShowPermissionRationale(it, activity) }) {
              activityResultApi34.launch(Constant.videoPermission)
            } else if (Constant.videoPermission.all { context.shouldShowPermissionRationale(it, activity) == false }) {
              context.openSetting(activityResult)
            }
            return@TopBarVideo
          }

          activityResultApi34.launch(Constant.videoPermission)

        }
      )
    }
  ) { innerPadding ->

    AnimatedContent(
      targetState = videoUiState.value,
      modifier = Modifier.padding(innerPadding),
      transitionSpec = { fadeIn().togetherWith(fadeOut()) },
      label = ""
    ) {

      when (it) {

        MediaStoreResult.Loading -> Loading(
          modifier = Modifier
            .fillMaxSize(),
        )

        MediaStoreResult.Empty -> EmptyVideoResultHandler(
          context = context,
          onRefreshVideoList = { onRefreshVideoList() },
        )

        is MediaStoreResult.Result -> {
          if (it.result.isNotEmpty()) {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              contentPadding = PaddingValues(top = 4.dp)
            ) {
              itemsIndexed(
                items = it.result,
                key = { index, _ -> it.result[index].videoId },
              ) { index, videoMediaModel ->
                VideoMediaItem(
                  item = videoMediaModel,
                  onItemClick = {
                    navHostController.navigate(MainNavRoute.VideoPlayerScreen("")) {
                      launchSingleTop = false
                    }
                    videoPageViewModel.startPlay(index, it.result)
                  },
                )
              }
            }
          }
        }

        else -> {}
      }
    }
  }
}
