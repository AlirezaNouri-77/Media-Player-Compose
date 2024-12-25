package com.example.mediaplayerjetpackcompose.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mediaplayerjetpackcompose.data.util.Constant.musicPermission
import com.example.mediaplayerjetpackcompose.presentation.screen.component.navigation.MainNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.getActivity
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.isPermissionGrant
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.openSetting
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.VideoPlayer
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    setContent {
      MediaPlayerJetpackComposeTheme {
        Surface(
          color = MaterialTheme.colorScheme.background,
        ) {

          var permissionState by remember {
            mutableStateOf(PermissionState.Initial)
          }

          var context = LocalContext.current

          intent?.let { mIntent ->
            if (mIntent.action == Intent.ACTION_VIEW) {
              val videoUri = mIntent.data ?: Uri.EMPTY
              val videoPageViewModel: VideoPageViewModel = koinViewModel()
              VideoPlayer(
                videoUri = videoUri.toString(),
                videoPageViewModel = videoPageViewModel,
                onBackClick = {
                  this.finishAffinity()
                }
              )
            } else {

              CheckPermission(
                permission = musicPermission,
                shouldShowPermissionRationale = {
                  permissionState = PermissionState.ShouldShowRationale
                },
                onGrant = {
                  permissionState = PermissionState.Grant
                },
                onDenied = {
                  permissionState = PermissionState.NotGrant
                },
              )

              when (permissionState) {
                PermissionState.Initial -> {}

                PermissionState.Grant -> MainNavController()

                PermissionState.NotGrant -> {

                  var activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (context.isPermissionGrant(musicPermission)) permissionState = PermissionState.Grant
                  }

                  ShowMessage(
                    message = "The permission to access music is denied",
                    actionMessage = "Open Setting",
                    onAction = {
                      context.openSetting(activityResult)
                    }
                  )

                }

                PermissionState.ShouldShowRationale -> {

                  val activityResult =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                      permissionState = if (it) PermissionState.Grant else PermissionState.NotGrant
                    }

                  ShowMessage(
                    message = "The permission to access music is not granted",
                    actionMessage = "Grant",
                    onAction = {
                      activityResult.launch(musicPermission)
                    },
                  )

                }

              }
            }
          }

        }

      }

    }

  }
}

@Composable
private fun ShowMessage(
  modifier: Modifier = Modifier,
  message: String,
  actionMessage: String,
  onAction: () -> Unit,
) {
  Box(
    modifier = modifier
      .fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = message,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
      )
      TextButton(
        onClick = {
          onAction()
        },
        border = BorderStroke(
          width = 1.dp,
          color = MaterialTheme.colorScheme.onPrimary,
        ),
      ) {
        Text(text = actionMessage, color = MaterialTheme.colorScheme.onPrimary)
      }
    }
  }
}

@Composable
fun CheckPermission(
  permission: String,
  shouldShowPermissionRationale: () -> Unit,
  onGrant: () -> Unit,
  onDenied: () -> Unit,
  context: Context = LocalContext.current,
) {

  val activityResult = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGrant ->
    when (isGrant) {
      true -> onGrant()
      false -> onDenied()
    }
  }

  when {
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
      onGrant()
    }

    context.getActivity()?.let { mContext -> ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission) } == true -> {
      shouldShowPermissionRationale()
    }

    context.getActivity()?.let { mContext -> ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission) } == false -> {
      SideEffect { activityResult.launch(permission) }
    }

    else -> SideEffect { activityResult.launch(permission) }
  }
}

enum class PermissionState {
  Initial, NotGrant, Grant, ShouldShowRationale
}

