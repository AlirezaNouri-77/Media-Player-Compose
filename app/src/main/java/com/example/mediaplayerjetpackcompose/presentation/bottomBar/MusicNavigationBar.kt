package com.example.mediaplayerjetpackcompose.presentation.bottomBar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavKey
import com.example.mediaplayerjetpackcompose.presentation.navigation.MusicTopLevel

@Composable
fun MusicNavigationBar(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    topLevel: NavKey,
    navigateTo: (NavKey) -> Unit,
    bottomBarGradientColor: Color,
    bottomSheetSwapFraction: () -> Float,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(200, delayMillis = 90)) + slideInVertically(
            animationSpec = tween(200, 90),
            initialOffsetY = { int -> int / 2 },
        ),
        exit = slideOutVertically(
            animationSpec = tween(400, 100),
            targetOffsetY = { int -> int / 2 },
        ) + fadeOut(tween(200, delayMillis = 90)),
    ) {
        NavigationBar(
            modifier = modifier
                .drawWithCache {
                    onDrawBehind {
                        drawContext
                        drawRect(
                            brush = Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.1f to bottomBarGradientColor.copy(alpha = 0.5f),
                                0.3f to bottomBarGradientColor.copy(alpha = 0.8f),
                                1f to bottomBarGradientColor.copy(alpha = 1f),
                            ),
                            alpha = 1f - bottomSheetSwapFraction(),
                            topLeft = Offset(
                                x = 0f,
                                y = this.size.height * bottomSheetSwapFraction(),
                            ),
                        )
                    }
                }
                .graphicsLayer {
                    translationY =
                        this@graphicsLayer.size.height * bottomSheetSwapFraction()
                }
                .onGloballyPositioned { Log.d("TAG3123", "MusicNavigationBar: " + density.run { it.size.height.toDp() }) },
            containerColor = Color.Transparent,
        ) {
            MusicTopLevel.entries.forEachIndexed { _, item ->
                val isSelected = item.route == topLevel
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { navigateTo(item.route) },
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(item.title) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        selectedIconColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        unselectedIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    ),
                )
            }
        }
    }
}
