package com.example.core.designsystem.util

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.core.model.WindowSize

@Composable
fun CurrentWindowSizeState(): WindowSize {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    return when {
        windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> WindowSize.EXPANDED
        windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> WindowSize.MEDIUM
        else -> WindowSize.COMPACT
    }
}
