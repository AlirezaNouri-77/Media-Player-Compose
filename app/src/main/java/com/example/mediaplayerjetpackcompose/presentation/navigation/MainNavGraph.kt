package com.example.mediaplayerjetpackcompose.presentation.navigation

import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.feature.video.VideoPage
import com.example.feature.video.VideoPageViewModel
import com.example.feature.video.playerScreen.VideoPlayer
import com.example.mediaplayerjetpackcompose.presentation.MainScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavGraph(
  window: Window? = LocalActivity.current?.window,
) {

  val navHostController: NavHostController = rememberNavController()
  val navBackStackEntry by navHostController.currentBackStackEntryAsState()

  window?.let {
    if (navBackStackEntry?.destination?.hasRoute(MainNavRoute.VideoPlayerScreen::class) == true) {
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


  NavHost(
    modifier = Modifier
      .fillMaxSize(),
    navController = navHostController,
    startDestination = MainNavRoute.MusicScreen,
  ) {

    composable<MainNavRoute.VideoScreen>(
      enterTransition = {
        fadeIn(tween(200, 20))
      },
      exitTransition = {
        fadeOut(tween(200, 20))
      },
      popEnterTransition = {
        fadeIn(tween(200, 20))
      },
      popExitTransition = {
        fadeOut(tween(200, 20))
      },
    ) {
      val videoPageViewModel: VideoPageViewModel = koinViewModel()

      VideoPage(
        videoPageViewModel = videoPageViewModel,
        onNavigateToVideoPlayer = {
          navHostController.navigate(MainNavRoute.VideoPlayerScreen("")) {
            launchSingleTop = false
          }
        },
        onNavigateToMusicScreen = {
          navHostController.popBackStack()
        },
      )
    }

    composable<MainNavRoute.VideoPlayerScreen>(
      enterTransition = {
        fadeIn(tween(200))
      },
      exitTransition = {
        fadeOut(tween(200))
      },
      popEnterTransition = {
        fadeIn(tween(200))
      },
      popExitTransition = {
        fadeOut(tween(200))
      },
    ) { backStackEntry ->
      Surface(
        color = Color.Black
      ) {
        val videoPageViewModel: VideoPageViewModel = koinViewModel()
        val videoPlayer = backStackEntry.toRoute<MainNavRoute.VideoPlayerScreen>()

        VideoPlayer(
          videoUri = videoPlayer.videoUri,
          videoPageViewModel = videoPageViewModel,
          onBackClick = {
            navHostController.popBackStack(
              MainNavRoute.VideoScreen, inclusive = false
            )
          },
        )
      }
    }

    composable<MainNavRoute.MusicScreen>(
      enterTransition = {
        fadeIn(tween(200, 20))
      },
      exitTransition = {
        fadeOut(tween(200, 20))
      },
      popEnterTransition = {
        fadeIn(tween(200, 20))
      },
      popExitTransition = {
        fadeOut(tween(200, 20))
      },
    ) {
      MainScreen(
        onNavigateToVideoScreen = {
          navHostController.navigate(MainNavRoute.VideoScreen)
        }
      )
    }
  }


}