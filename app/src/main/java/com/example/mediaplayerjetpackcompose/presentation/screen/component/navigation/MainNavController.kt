package com.example.mediaplayerjetpackcompose.presentation.screen.component.navigation

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MainScreenNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.VideoPlayer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavController(
  window: Window? = LocalActivity.current?.window,
) {

  val musicPageViewModel: MusicPageViewModel = koinViewModel()
  val videoPageViewModel: VideoPageViewModel = koinViewModel()

  val navHostController: NavHostController = rememberNavController()
  val navBackStackEntry by navHostController.currentBackStackEntryAsState()

  window?.let {
    if (navBackStackEntry?.destination?.hasRoute(MainScreenNavigationModel.VideoPlayerScreen::class) == true) {
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
    startDestination = MainScreenNavigationModel.MusicScreen,
  ) {

    composable<MainScreenNavigationModel.VideoScreen>(
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
      VideoPage(
        navHostController = navHostController,
        videoPageViewModel = videoPageViewModel,
        onNavigateToMusicScreen = {
          navHostController.popBackStack()
        },
      )
    }

    composable<MainScreenNavigationModel.VideoPlayerScreen>(
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
        val videoPlayer = backStackEntry.toRoute<MainScreenNavigationModel.VideoPlayerScreen>()
        VideoPlayer(
          videoUri = videoPlayer.videoUri,
          videoPageViewModel = videoPageViewModel,
          onBackClick = {
            navHostController.popBackStack(
              MainScreenNavigationModel.VideoScreen, inclusive = false
            )
          },
        )
      }
    }

    composable<MainScreenNavigationModel.MusicScreen>(
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
      MusicScreen(
        musicPageViewModel = musicPageViewModel,
        onNavigateToVideoScreen = {
          navHostController.navigate(MainScreenNavigationModel.VideoScreen)
        }
      )
    }
  }


}