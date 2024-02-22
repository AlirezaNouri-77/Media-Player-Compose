package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.domain.model.bottomNavigation.BottomNavigationItem
import com.example.mediaplayerjetpackcompose.presentation.util.NoRippleEffect

@Composable
fun BottomNavigationBar(
  currentRoute: String,
  onClick: (String) -> Unit,
) {

  val bottomNavigationItemList: List<BottomNavigationItem> = listOf(
    BottomNavigationItem.Video,
    BottomNavigationItem.Music
  )

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
  ) {
    bottomNavigationItemList.forEach {
      val icon = when (currentRoute == it.route.route) {
        true -> it.iconFilled
        else -> it.iconOutline
      }
      NavigationBarItem(
        selected = currentRoute == it.route.route,
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = Color.Transparent,
        ),
        onClick = {
          if (currentRoute != it.route.route) {
            onClick.invoke(it.route.route)
          }
        },
        icon = {
          Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
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