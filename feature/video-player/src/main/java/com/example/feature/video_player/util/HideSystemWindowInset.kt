package com.example.feature.video_player.util

import android.view.Window
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun HideSystemWindowInset(
  window: Window? = LocalActivity.current?.window,
) {
  DisposableEffect(Unit) {
    window ?: return@DisposableEffect onDispose {}
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)

    insetsController.apply {
      hide(WindowInsetsCompat.Type.statusBars())
      hide(WindowInsetsCompat.Type.systemBars())
      systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    onDispose {
      insetsController.apply {
        show(WindowInsetsCompat.Type.statusBars())
        show(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
      }
    }
  }
}