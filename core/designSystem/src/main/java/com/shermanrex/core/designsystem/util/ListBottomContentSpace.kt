package com.shermanrex.core.designsystem.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun listBottomContentSpace(considerMiniPlayer: Boolean = false): Dp {
    val windowSize = calculateWindowSize()
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    return if (windowSize == DeviceSize.COMPACT) {
        NavigationBottomBarHeight + navigationBarHeight + if (considerMiniPlayer) MiniPlayerHeight else 0.dp + 24.dp
    } else {
        navigationBarHeight
    }
}
