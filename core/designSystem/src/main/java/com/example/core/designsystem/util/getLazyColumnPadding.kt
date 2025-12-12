package com.example.core.designsystem.util

import androidx.compose.ui.unit.dp

fun getLazyColumnPadding(considerMiniPlayer: Boolean = false) = NavigationBottomBarHeight + if (considerMiniPlayer) MiniPlayerHeight else 0.dp
