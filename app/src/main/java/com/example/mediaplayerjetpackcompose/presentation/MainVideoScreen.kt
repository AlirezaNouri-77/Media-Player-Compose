package com.example.mediaplayerjetpackcompose.presentation

import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.feature.video.VideoPage
import com.example.feature.video.VideoPageViewModel
import com.example.feature.video_player.VideoPlayer
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainVideoScreen(
  modifier: Modifier = Modifier,
  videoPageViewModel: VideoPageViewModel = koinViewModel(),
  onBack: () -> Unit,
  window: Window? = LocalActivity.current?.window,
) {

  var navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val videoUiState = videoPageViewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(navBackStackEntry?.destination?.route) {
    window?.let {
      if (navBackStackEntry?.destination?.hasRoute(VideoScreenRoutes.VideoPlayer::class) == true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          it.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            hide(WindowInsets.Type.systemBars())
            this.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
          }
        }
      } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          it.insetsController?.apply {
            show(WindowInsets.Type.statusBars())
            show(WindowInsets.Type.systemBars())
          }
        }
      }
    }
  }

  NavHost(
    modifier = Modifier.fillMaxSize(),
    navController = navController,
    startDestination = VideoScreenRoutes.VideoPage
  ) {

    composable<VideoScreenRoutes.VideoPage> {
      VideoPage(
        modifier = modifier,
        videoUiState = { videoUiState },
        onRefreshVideoList = videoPageViewModel::getVideo,
        onPlay = { index, list ->
          navController.navigate(VideoScreenRoutes.VideoPlayer)
          videoPageViewModel.startPlay(index, list)
        },
        onBack = { onBack() },
      )
    }

    composable<VideoScreenRoutes.VideoPlayer> {
      VideoPlayer(
        videoUri = "",
        videoPageViewModel = videoPageViewModel,
        onBack = {
          videoPageViewModel.stopPlayer()
          navController.popBackStack()
        },
      )
    }

  }


}

sealed interface VideoScreenRoutes {
  @Serializable
  data object VideoPage : VideoScreenRoutes
  @Serializable
  data object VideoPlayer : VideoScreenRoutes
}