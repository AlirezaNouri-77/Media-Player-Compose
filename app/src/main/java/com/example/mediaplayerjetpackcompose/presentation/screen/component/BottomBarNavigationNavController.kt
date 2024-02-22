package com.example.mediaplayerjetpackcompose.presentation.screen.component

import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.domain.model.navigation.BottomBarNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPlayerPage

@Composable
fun BottomBarNavigationNavController(
  navHostController: NavHostController,
  musicPageViewModel: MusicPageViewModel,
  videoPageViewModel: VideoPageViewModel,
) {

  val window = (LocalContext.current as Activity).window
  val currentRoute = navHostController.currentDestination?.route

  if (currentRoute == BottomBarNavigationModel.PlayerScreen.route) {
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
        this.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
      }
    }
  }

  NavHost(
    navController = navHostController,
    startDestination = BottomBarNavigationModel.MusicScreen.route,
  ) {

    composable(
      BottomBarNavigationModel.VideoScreen.route,
      enterTransition = {
        when (initialState.destination.route) {
          BottomBarNavigationModel.MusicScreen.route -> {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
            )
          }

          else -> {
            fadeIn(tween(200))
          }
        }
      },
      exitTransition = {
        when (initialState.destination.route) {
          BottomBarNavigationModel.MusicScreen.route -> {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
            )
          }

          else -> {
            fadeOut(tween(200))
          }
        }
      },
      popEnterTransition = {
        when (initialState.destination.route) {
          BottomBarNavigationModel.MusicScreen.route -> {
            slideIntoContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
            )
          }

          else -> {
            fadeIn(tween(200))
          }
        }
      },
      popExitTransition = {
        when (initialState.destination.route) {
          BottomBarNavigationModel.MusicScreen.route -> {
            slideOutOfContainer(
              towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
            )
          }

          else -> {
            fadeOut(tween(200))
          }
        }
      },
    ) {
      VideoPage(
        navHostController = navHostController,
        videoPageViewModel = videoPageViewModel,
      )
    }
    composable(
      BottomBarNavigationModel.PlayerScreen.route,
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
        VideoPlayerPage(
          videoUri = it.arguments?.getString("videoUri").toString(),
          videoPageViewModel = videoPageViewModel,
          onBackClick = {
            navHostController.popBackStack(
              BottomBarNavigationModel.VideoScreen.route, inclusive = false
            )
          },
        )
      }
    }
    composable(
      BottomBarNavigationModel.MusicScreen.route,
      enterTransition = {
        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
        )
      },
      popEnterTransition = {
        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
      },
      popExitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
        )
      },
    ) {
      MusicScreen(
        musicPageViewModel = musicPageViewModel,
      )
    }
  }

}