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
fun MainNavGraph() {
    val navHostController: NavHostController = rememberNavController()

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
                onBack = navHostController::popBackStack,
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
                },
            )
        }
    }
}
