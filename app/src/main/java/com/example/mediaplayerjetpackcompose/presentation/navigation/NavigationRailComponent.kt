package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.NavKey
import com.example.mediaplayerjetpackcompose.presentation.bottomBar.NavigationBarModel

@Composable
fun NavigationRailComponent(
    modifier: Modifier = Modifier,
    topLevel: NavKey,
    isVisible: Boolean,
    navController: NavHostController = rememberNavController(),
    onClick: (NavKey) -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    if (isVisible) {
        NavigationRail(
            containerColor = Color.Transparent,
            modifier = modifier.displayCutoutPadding(),
        ) {
            NavigationBarModel.entries.forEachIndexed { index, item ->
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
                        indicatorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        selectedIconColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        unselectedIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    ),
                )
            }
        }
    }
}
