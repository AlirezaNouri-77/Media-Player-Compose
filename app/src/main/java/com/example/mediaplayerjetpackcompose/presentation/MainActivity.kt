package com.example.mediaplayerjetpackcompose.presentation

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.domain.model.PermissionState
import com.example.feature.video.VideoPlayer
import com.example.feature.video.VideoViewModel
import com.example.feature.video.util.isPermissionGrant
import com.example.feature.video.util.openSetting
import com.example.mediaplayerjetpackcompose.presentation.component.CheckPermission
import com.example.mediaplayerjetpackcompose.presentation.component.ShowMessage
import com.example.mediaplayerjetpackcompose.presentation.navigation.NavMainGraph
import com.example.mediaplayerjetpackcompose.presentation.navigation.VideoPlayer
import com.example.mediaplayerjetpackcompose.presentation.util.Constant.musicPermission
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val systemBarStyle = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
            Configuration.UI_MODE_NIGHT_YES -> SystemBarStyle.dark(Color.Transparent.toArgb())
            else -> SystemBarStyle.dark(Color.Transparent.toArgb())
        }
        enableEdgeToEdge(statusBarStyle = systemBarStyle, navigationBarStyle = systemBarStyle)
        super.onCreate(savedInstanceState)

        setContent {
            MediaPlayerJetpackComposeTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var permissionState by remember {
                        mutableStateOf(PermissionState.Initial)
                    }

                    val context = LocalContext.current

                    intent?.let { mIntent ->
                        if (mIntent.action == Intent.ACTION_VIEW) {
                            val videoUri = mIntent.data ?: Uri.EMPTY
                            val videoViewModel: VideoViewModel = koinViewModel()
                            VideoPlayer(
                                videoUri = videoUri.toString(),
                                videoViewModel = videoViewModel,
                                onBack = {
                                    this.finishAffinity()
                                },
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

                                PermissionState.Grant -> NavMainGraph()

                                PermissionState.NotGrant -> {
                                    val activityResult =
                                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                                            if (context.isPermissionGrant(musicPermission)) {
                                                permissionState =
                                                    PermissionState.Grant
                                            }
                                        }

                                    ShowMessage(
                                        message = "The permission to access music is denied",
                                        actionMessage = "Open Setting",
                                        onAction = {
                                            context.openSetting(activityResult)
                                        },
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
