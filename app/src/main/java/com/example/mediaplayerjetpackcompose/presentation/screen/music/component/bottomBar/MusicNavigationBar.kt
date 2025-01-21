package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.bottomBar

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.navigation.NavigationBarModel

@Composable
fun MusicNavigationBar(
  modifier: Modifier = Modifier,
  navController: NavController,
  navigateTo: (MusicNavigationModel) -> Unit,
) {

  var navigationBarCurrentState = remember { mutableIntStateOf(0) }

  var backStackEntry = navController.currentBackStackEntryAsState()

  LaunchedEffect(backStackEntry.value?.destination) {
    var index = NavigationBarModel.entries.indexOfFirst { backStackEntry.value?.destination?.hasRoute(it.route::class) == true }
    if (index < 0) return@LaunchedEffect

    navigationBarCurrentState.intValue = index
  }

  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.secondary,
  ) {
    NavigationBarModel.entries.forEachIndexed { index, item ->
      NavigationBarItem(
        selected = navigationBarCurrentState.intValue == index,
        onClick = {
          var isDuplicateDestination = backStackEntry.value?.destination?.hasRoute(item.route::class) == true

          if (!isDuplicateDestination) {
            navigateTo(item.route)
          }
        },
        icon = {
          Icon(painter = painterResource(item.icon), contentDescription = null)
        },
        label = {
          Text(item.title)
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
          selectedIconColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
          unselectedIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )
      )
    }
  }
}