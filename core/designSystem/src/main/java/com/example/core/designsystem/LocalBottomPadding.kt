package com.example.core.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val MiniPlayerHeight = 70.dp
var LocalParentScaffoldPadding = compositionLocalOf { PaddingValues() }

var LocalMiniPlayerHeight = staticCompositionLocalOf { MiniPlayerHeight }