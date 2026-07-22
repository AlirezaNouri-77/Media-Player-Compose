package com.shermanrex.core.designsystem.util

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

@Composable
fun calculateWidthWindowSize(): DeviceSize {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    return when {
        windowSize.minWidthDp < WIDTH_DP_MEDIUM_LOWER_BOUND -> DeviceSize.COMPACT
        windowSize.minWidthDp in WIDTH_DP_MEDIUM_LOWER_BOUND..<WIDTH_DP_EXPANDED_LOWER_BOUND -> DeviceSize.COMPACT
        else -> DeviceSize.LARGE
    }
}

@Composable
fun calculateHeightWindowSize(): DeviceSize {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    return when {
        windowSize.minHeightDp < HEIGHT_DP_MEDIUM_LOWER_BOUND -> DeviceSize.COMPACT
        else -> DeviceSize.LARGE
    }
}

enum class DeviceSize {
    COMPACT,
    LARGE,
}
