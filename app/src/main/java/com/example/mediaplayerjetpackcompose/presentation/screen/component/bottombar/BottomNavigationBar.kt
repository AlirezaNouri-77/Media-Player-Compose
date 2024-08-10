package com.example.mediaplayerjetpackcompose.presentation.screen.component.bottombar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.bottomNavigation.BottomNavigationItem

@Composable
fun BottomNavigationBar(
  currentRoute: () -> String?,
  onClick: (String) -> Unit,
) {

  val bottomNavigationItemList: List<BottomNavigationItem> = remember {
    listOf(
      BottomNavigationItem.Video,
      BottomNavigationItem.Music
    )
  }

  NavigationBar(
    modifier = Modifier
      .fillMaxWidth(),
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimary,
  ) {

    bottomNavigationItemList.forEach {

      val iconAlpha = remember(currentRoute()) {
        if (currentRoute() == it.route.route) 1f else 0.4f
      }

      NavigationBarItem(
        selected = currentRoute() == it.route.route,
        alwaysShowLabel = false,
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = Color.Transparent,
        ),
        onClick = { if (currentRoute() != it.route.route) onClick.invoke(it.route.route) },
        icon = {
          Icon(
            painter = painterResource(id = it.icon),
            contentDescription = "",
            modifier = Modifier
              .size(25.dp)
              .graphicsLayer { alpha = iconAlpha },
            tint = MaterialTheme.colorScheme.onPrimary,
          )
        },
      )
    }

  }
}