package com.example.mediaplayerjetpackcompose.presentation.screen.component.bottombar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
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
  currentRoute: String?,
  onClick: (String) -> Unit,
) {

  val bottomNavigationItemList: List<BottomNavigationItem> = remember {
    listOf(
      BottomNavigationItem.Video,
      BottomNavigationItem.Music
    )
  }

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
  ) {
    bottomNavigationItemList.forEach {

      val iconAlpha = if (currentRoute == it.route.route) 1f else 0.5f

      NavigationBarItem(
        selected = currentRoute == it.route.route,
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = Color.Transparent,
        ),
        onClick = {
          onClick.invoke(it.route.route)
        },
        icon = {
          Icon(
            painter = painterResource(id = it.icon),
            contentDescription = "",
            modifier = Modifier
              .size(25.dp)
              .graphicsLayer { alpha = iconAlpha },
            tint = MaterialTheme.colorScheme.onPrimary
          )
        },
        label = {
          Text(
            text = it.title,
            color = MaterialTheme.colorScheme.onPrimary,
          )
        }
      )
    }
  }

}