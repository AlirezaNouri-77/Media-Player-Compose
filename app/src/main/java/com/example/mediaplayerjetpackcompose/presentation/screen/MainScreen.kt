package com.example.mediaplayerjetpackcompose.presentation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.player.PlayerScreen
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.BottomNavigationBar

@Composable
fun MainScreen(
  navHostController: NavHostController = rememberNavController(),
) {

  navHostController.currentBackStackEntryFlow.collectAsStateWithLifecycle(
    initialValue = NavigationRoute.VideoScreen
  ).value
  val currentRoute = navHostController.currentDestination?.route
  val videoPageViewModel: VideoPageViewModel = viewModel(factory = VideoPageViewModel.Factory)
  val musicPageViewModel: MusicPageViewModel = viewModel(factory = MusicPageViewModel.Factory)

  val topBarTitle = when (currentRoute) {
    NavigationRoute.VideoScreen.route -> "Video"
    NavigationRoute.MusicScreen.route -> "Music"
    else -> ""
  }

  val isDataLoaded = true

  if (isDataLoaded) {

    Scaffold(
      bottomBar = {
        if (currentRoute != null && currentRoute != NavigationRoute.PlayerScreen.route) {
          BottomNavigationBar(
            currentRoute = currentRoute,
            onClick = {
              navHostController.navigate(it)
            },
          )
        }
      },
    ) { paddingValues ->

      NavController(
        navHostController = navHostController,
        musicPageViewModel = musicPageViewModel,
        videoPageViewModel = videoPageViewModel,
        padding = paddingValues,
        modifier = Modifier
      )

    }

  } else {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(text = "Simple Media Player", fontSize = 30.sp, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(35.dp))
      CircularProgressIndicator()
      Spacer(modifier = Modifier.height(5.dp))
      Text(text = "Loading")
    }
  }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavController(
  navHostController: NavHostController,
  musicPageViewModel: MusicPageViewModel,
  videoPageViewModel: VideoPageViewModel,
  padding: PaddingValues,
  modifier: Modifier,
) {

  NavHost(
    navController = navHostController,
    startDestination = NavigationRoute.VideoScreen.route,
    modifier = modifier.padding(padding),
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
    composable(NavigationRoute.PlayerScreen.route, enterTransition = {
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

sealed class NavigationRoute(var route: String) {
  data object VideoScreen : NavigationRoute("VideoScreen")
  data object MusicScreen : NavigationRoute("MusicScreen")
  data object PlayerScreen : NavigationRoute("PlayerScreen/{videoUri}")
}