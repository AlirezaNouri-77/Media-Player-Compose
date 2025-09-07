package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigationBarNavigate(
    backStackEntry: NavBackStackEntry?,
    route: MusicTopLevelDestination,
) {
    val isDuplicateDestination =
        backStackEntry?.destination?.hasRoute(route::class) == true
    val currentDestination = this.currentDestination?.id
        ?: this.graph.findStartDestination().id
    if (!isDuplicateDestination) {
        this.navigate(route) {
            this.popUpTo(currentDestination) {
                inclusive = true
                saveState = true
            }
            restoreState = true
            this.launchSingleTop = true
        }
    }
}
