package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerjetpackcompose.presentation.MainMusicScreen
import com.example.mediaplayerjetpackcompose.presentation.MainVideoScreen

@Composable
fun MainNavGraph(
// window: Window? = LocalActivity.current?.window,
) {

  val navHostController: NavHostController = rememberNavController()
//  val navBackStackEntry by navHostController.currentBackStackEntryAsState()

//  window?.let {
//    if (navBackStackEntry?.destination?.hasRoute(MainNavRoute.VideoPlayerScreen::class) == true) {
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        it.insetsController?.apply {
//          hide(WindowInsets.Type.statusBars())
//          hide(WindowInsets.Type.systemBars())
//          this.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//      }
//    } else {
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        it.insetsController?.apply {
//          show(WindowInsets.Type.statusBars())
//          show(WindowInsets.Type.systemBars())
//        }
//      }
//    }
//  }


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
      MainVideoScreen(
        onBack = {
          navHostController.popBackStack()
        }
      )
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
      MainMusicScreen(
        onNavigateToVideoScreen = {
          navHostController.navigate(MainNavRoute.VideoScreen)
        }
      )
    }

  }

}