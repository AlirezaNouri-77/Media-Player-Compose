package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.bottomBar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.mediaplayerjetpackcompose.domain.model.navigation.MusicNavigationModel
import com.example.mediaplayerjetpackcompose.domain.model.navigation.NavigationBarModel

@Composable
fun MusicNavigationBar(
  modifier: Modifier = Modifier,
  navigateTo: (MusicNavigationModel) -> Unit,
) {

  var navigationBarCurrentState by remember { mutableIntStateOf(0) }

  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.secondary,
  ) {
    NavigationBarModel.entries.forEachIndexed { index, item ->
      NavigationBarItem(
        selected = index == navigationBarCurrentState,
        onClick = {
          navigateTo(item.route)
          navigationBarCurrentState = index
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