package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.presentation.screen.NavigationRoute
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.player.PlayerScreen

@Composable
fun BottomNavigationNavController(
  navHostController: NavHostController,
  musicPageViewModel: MusicPageViewModel,
  videoPageViewModel: VideoPageViewModel,
) {
  NavHost(
    navController = navHostController,
    startDestination = NavigationRoute.MusicScreen.route,
  ) {
    composable(
      NavigationRoute.VideoScreen.route,
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
        )
      },
      popEnterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
        )
      },
      popExitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
        )
      },
    ) {
      VideoPage(
        navHostController = navHostController,
        videoItemList = videoPageViewModel.mediaStoreDataList,
      )
    }
    composable(
      NavigationRoute.PlayerScreen.route, enterTransition = {
      slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
    }, exitTransition = {
      slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
      )
    }, popEnterTransition = {
      slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
    }, popExitTransition = {
      slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
      )
    }, arguments = listOf(
      navArgument(name = "videoUri") {
        type = NavType.StringType
      },
    )
    ) {
      PlayerScreen(
        videoUri = it.arguments!!.getString("videoUri").toString(),
        onBackClick = {
          navHostController.popBackStack(
            NavigationRoute.VideoScreen.route, inclusive = false
          )
        },
      )
    }
    composable(
      NavigationRoute.MusicScreen.route,
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