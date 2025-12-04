package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavKey

@Composable
fun NavigationRailComponent(
    modifier: Modifier = Modifier,
    topLevel: NavKey,
    isVisible: Boolean,
    onClick: (NavKey) -> Unit,
) {
    if (isVisible) {
        NavigationRail(
            containerColor = Color.Transparent,
            modifier = modifier.displayCutoutPadding(),
        ) {
            MusicTopLevel.entries.forEach { item ->
                val isSelected = item.route == topLevel
                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onClick(item.route) },
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(item.title) },
                    alwaysShowLabel = true,
                    colors = NavigationRailItemDefaults.colors(
                        unselectedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        selectedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        indicatorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        selectedIconColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        unselectedIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    ),
                )
            }
        }
    }
}
