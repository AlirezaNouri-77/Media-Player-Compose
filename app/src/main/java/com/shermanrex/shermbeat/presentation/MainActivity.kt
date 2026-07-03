package com.shermanrex.shermbeat.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.domain.model.PermissionState
import com.shermanrex.feature.video.VideoPlayer
import com.shermanrex.feature.video.VideoViewModel
import com.shermanrex.feature.video.util.isPermissionGrant
import com.shermanrex.feature.video.util.openSetting
import com.shermanrex.shermbeat.presentation.component.CheckPermission
import com.shermanrex.shermbeat.presentation.component.ShowMessage
import com.shermanrex.shermbeat.presentation.navigation.NavMainGraph
import com.shermanrex.shermbeat.presentation.util.Constant.musicPermission
import com.shermanrex.shermbeat.presentation.util.enableEdgeToEdgeScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeScreen(resources = resources)
        installSplashScreen()
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
