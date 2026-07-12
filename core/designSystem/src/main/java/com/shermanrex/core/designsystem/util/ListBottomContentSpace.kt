package com.shermanrex.core.designsystem.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun listBottomContentSpace(considerMiniPlayer: Boolean = false): Dp {
    val density = LocalDensity.current
    val navigationBarHeight = with(density) {
        WindowInsets.navigationBars.getBottom(density).toDp()
    }
    return NavigationBottomBarHeight + navigationBarHeight + if (considerMiniPlayer) MiniPlayerHeight else 0.dp
}
