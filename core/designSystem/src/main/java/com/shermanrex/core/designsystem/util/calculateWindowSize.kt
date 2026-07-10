package com.shermanrex.core.designsystem.util

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.shermanrex.core.model.WindowSize

@Composable
fun calculateWindowSize(): WindowSize {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    return when {
        windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> WindowSize.EXPANDED
        windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> WindowSize.MEDIUM
        else -> WindowSize.COMPACT
    }
}
