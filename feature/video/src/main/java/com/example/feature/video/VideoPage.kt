package com.example.feature.video

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
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.Loading
import com.example.core.model.MediaStoreResult
import com.example.core.model.VideoModel
import com.example.feature.video.component.EmptyVideoResultHandler
import com.example.feature.video.component.TopBarVideo
import com.example.feature.video.item.VideoMediaItem
import com.example.feature.video.util.Constant

@Composable
fun VideoPage(
  modifier: Modifier = Modifier,
  videoUiState: () -> State<MediaStoreResult<VideoModel>>,
  onRefreshVideoList: () -> Unit,
  onPlay: (Int, List<VideoModel>) -> Unit,
  onBack: () -> Unit,
  context: Context = LocalContext.current,
  activity: Activity? = LocalActivity.current,
) {

  val activityResultApi34 = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
    onRefreshVideoList()
  }

  val activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    onRefreshVideoList()
  }

  Scaffold(
    modifier = modifier,
    topBar = {
      TopBarVideo(
        context = context,
        onBackClick = onBack,
        onSelectVideo = {
          val isPermissionsGrant = Constant.videoPermission.all { context.isPermissionDenied(it) }

          if (isPermissionsGrant) {
            if (Constant.videoPermission.all {
                context.shouldShowPermissionRationale(
                  it,
                  activity
                )
              }) {
              activityResultApi34.launch(Constant.videoPermission)
            } else if (Constant.videoPermission.all { !context.shouldShowPermissionRationale(it, activity) }) {
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
      targetState = videoUiState().value,
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
                    onPlay(index, it.result)
                  },
                )
              }
            }
          }
        }

      }
    }
  }
}
