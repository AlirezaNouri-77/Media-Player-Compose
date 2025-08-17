package com.example.mediaplayerjetpackcompose.presentation.bottomBar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.core.model.navigation.MusicNavigationRoute

@Composable
fun MusicNavigationBar(
  modifier: Modifier = Modifier,
  navController: NavController,
  navigateTo: (MusicNavigationRoute) -> Unit,
) {

  var navigationBarCurrentIndex by rememberSaveable { mutableIntStateOf(0) }
  val backStackEntry by navController.currentBackStackEntryAsState()

  LaunchedEffect(backStackEntry?.destination) {
    val index = TopLevelNavigation.entries.indexOfFirst { backStackEntry?.destination?.hasRoute(it.route::class) == true }
    if (index < 0) return@LaunchedEffect

    navigationBarCurrentIndex = index
  }

  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.secondary,
  ) {
    TopLevelNavigation.entries.forEachIndexed { index, item ->
      NavigationBarItem(
        selected = navigationBarCurrentIndex == index,
        onClick = {
          val isInSameRoute = backStackEntry?.destination?.hasRoute(item.route::class) == true
          if (!isInSameRoute) { navigateTo(item.route) }
        },
        icon = { Icon(painter = painterResource(item.icon), contentDescription = null) },
        label = { Text(item.title) },
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