package com.example.mediaplayerjetpackcompose.presentation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
  
  val navigationHandler = rememberNavController()
  navigationHandler.currentBackStackEntryFlow.collectAsStateWithLifecycle(
	initialValue = NavigationRoute.MediaScreen
  ).value
  val currentRoute = navigationHandler.currentDestination?.route
  
  Scaffold(
	topBar = {
	  if (currentRoute==NavigationRoute.MediaScreen.route){
		TopAppBar(title = { Text(text = "Video", fontSize = 22.sp, fontWeight = FontWeight.Bold,)})
	  }
	}
  ){
	Box(modifier = Modifier.padding(it)){
	  NavController(navHostController = navigationHandler)
	}
  }
  
}

@Composable
fun NavController(
  navHostController: NavHostController
) {
  
  
  NavHost(navController = navHostController, startDestination = NavigationRoute.MediaScreen.route) {
	composable(
	  NavigationRoute.MediaScreen.route,
	  enterTransition = {
		slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
	  },
	  exitTransition = {
		slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
	  },
	  popEnterTransition = {
		slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
	  },
	  popExitTransition = {
		slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
	  },
	) {
	  MediaScreen(navHostController = navHostController)
	}
	composable(
	  NavigationRoute.PlayerScreen.route,
	  enterTransition = {
		slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
	  },
	  exitTransition = {
		slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
	  },
	  popEnterTransition = {
		slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
	  },
	  popExitTransition = {
		slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
	  },
	  arguments = listOf(
		navArgument(name = "videoUri") {
		  type = NavType.StringType
		}
	  )
	) {
	  PlayerScreen(
		videoUri = it.arguments!!.getString("videoUri").toString(),
		navHostController = navHostController,
	  )
	}
  }
}

sealed class NavigationRoute(var route: String) {
  data object MediaScreen : NavigationRoute("MediaScreen")
  data object PlayerScreen : NavigationRoute("PlayerScreen/{videoUri}")
}