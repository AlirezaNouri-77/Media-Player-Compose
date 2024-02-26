package com.example.mediaplayerjetpackcompose.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediaplayerjetpackcompose.data.Constant.permissionsList
import com.example.mediaplayerjetpackcompose.data.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.presentation.screen.MainScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.NoPermissionPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.component.VideoPlayer
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {

    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
      navigationBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
    )
    super.onCreate(savedInstanceState)

    setContent {
      MediaPlayerJetpackComposeTheme {
        val uiState = remember { mutableStateOf(PermissionState.Initial) }

        if (checkPermission(this)) {
          uiState.value = PermissionState.PermissionIsGrant
        }

        PermissionHandler(
          context = this,
          onGrant = { uiState.value = PermissionState.PermissionIsGrant },
          notGrant = { uiState.value = PermissionState.PermissionNotGrant },
          permissionsList = permissionsList,
        )

        when (uiState.value) {
          PermissionState.PermissionNotGrant -> {
            NoPermissionPage(
              onGrant = {
                uiState.value = PermissionState.PermissionIsGrant
              }
            )
          }

          PermissionState.PermissionIsGrant -> {
            intent?.let { mIntent ->
              if (mIntent.action == Intent.ACTION_VIEW) {
                val videoUri = mIntent.data.toString().encodeStringNavigation()
                val musicPageViewModel = viewModel<VideoPageViewModel>(factory = VideoPageViewModel.Factory)
                VideoPlayer(
                  videoUri = videoUri,
                  videoPageViewModel = musicPageViewModel,
                  onBackClick = { this.finishAffinity() })
              } else {
                MainScreen()
              }
            }
          }

          else -> {}
        }

      }

    }

  }

}

@Composable
fun PermissionHandler(
  context: Context,
  onGrant: () -> Unit,
  notGrant: () -> Unit,
  permissionsList: Array<String>
) {

  val requestPermission = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) {
    val areGranted = it.values.reduce { acc, next -> acc && next }
    if (areGranted) {
      onGrant.invoke()
    } else {
      notGrant.invoke()
    }
  }

  LaunchedEffect(!checkPermission(context)) {
    requestPermission.launch(permissionsList)
  }

}

fun checkPermission(context: Context): Boolean {

  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

    myPermissionChecker(context,Manifest.permission.READ_MEDIA_AUDIO)
         && myPermissionChecker(context,Manifest.permission.READ_MEDIA_VIDEO)

  } else {
    myPermissionChecker(
      context,
      Manifest.permission.READ_EXTERNAL_STORAGE
    )
  }
}

//fun hideStatusBar(context: Context) {
//  val window = (context as Activity).window
//  WindowCompat.setDecorFitsSystemWindows(window, false)
//  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//    window.insetsController?.apply {
//      hide(WindowInsets.Type.statusBars())
//      this.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//    }
//  }
//}

fun myPermissionChecker(context: Context, permission: String): Boolean {
  return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

enum class PermissionState {
  Initial, PermissionNotGrant, PermissionIsGrant
}

