package com.example.mediaplayerjetpackcompose.presentation.screen.component.navigation

import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.domain.model.navigation.NavigationRouteModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.VideoPlayer


@Composable
fun ScreenNavController(
  navHostController: NavHostController,
  musicPageViewModel: MusicPageViewModel,
  videoPageViewModel: VideoPageViewModel,
  window: Window = (LocalContext.current as Activity).window
) {

  val navBackStackEntry by navHostController.currentBackStackEntryAsState()
  val currentRoute = remember(navBackStackEntry) {
    navBackStackEntry?.destination?.route
  }

  if (currentRoute == NavigationRouteModel.VideoPlayerScreen.route) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.insetsController?.apply {
        hide(WindowInsets.Type.statusBars())
        hide(WindowInsets.Type.systemBars())
        this.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
      }
    }
  } else {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.insetsController?.apply {
        show(WindowInsets.Type.statusBars())
        show(WindowInsets.Type.systemBars())
    //    this.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
      }
    }
  }

  NavHost(
    modifier = Modifier
      .fillMaxSize(),
    navController = navHostController,
    startDestination = NavigationRouteModel.MusicScreen.route,
  ) {

    composable(
      NavigationRouteModel.VideoScreen.route,
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
          navHostController.navigate(NavigationRouteModel.MusicScreen.route)
        }
      )
    }

    composable(
      NavigationRouteModel.VideoPlayerScreen.route,
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
      }, arguments = listOf(
        navArgument(name = "videoUri") {
          type = NavType.StringType
          nullable = true
          defaultValue = ""
        },
      )
    ) {
      Surface(
        color = Color.Black
      ) {
        VideoPlayer(
          videoUri = it.arguments?.getString("videoUri").toString(),
          videoPageViewModel = videoPageViewModel,
          onBackClick = {
            navHostController.popBackStack(
              NavigationRouteModel.VideoScreen.route, inclusive = false
            )
          },
        )
      }
    }

    composable(
      NavigationRouteModel.MusicScreen.route,
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
          navHostController.navigate(NavigationRouteModel.VideoScreen.route)
        }
      )
    }
  }

}