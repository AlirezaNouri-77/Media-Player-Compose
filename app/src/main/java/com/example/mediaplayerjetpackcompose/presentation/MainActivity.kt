package com.example.mediaplayerjetpackcompose.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.mediaplayerjetpackcompose.data.util.Constant.permissionsList
import com.example.mediaplayerjetpackcompose.data.util.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.presentation.screen.RootScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.component.NoPermissionPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.VideoPlayer
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
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
                val videoPageViewModel: VideoPageViewModel by viewModel()
                VideoPlayer(
                  videoUri = videoUri,
                  videoPageViewModel = videoPageViewModel,
                  onBackClick = { this.finishAffinity() })
              } else {
                RootScreen()
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

    myPermissionChecker(context, Manifest.permission.READ_MEDIA_AUDIO)
         && myPermissionChecker(context, Manifest.permission.READ_MEDIA_VIDEO)

  } else {
    myPermissionChecker(
      context,
      Manifest.permission.READ_EXTERNAL_STORAGE
    )
  }
}

fun myPermissionChecker(context: Context, permission: String): Boolean {
  return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

enum class PermissionState {
  Initial, PermissionNotGrant, PermissionIsGrant
}

