package com.example.mediaplayerjetpackcompose.presentation.screen.video.component

import android.app.Activity
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.util.openSetting
import com.example.mediaplayerjetpackcompose.util.shouldShowPermissionRationale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionApi33ToLowerHandler(
  context: Context,
  message: String,
  permission: String,
  onRefreshVideo: () -> Unit,
  onGrant: () -> Unit,
  activity: Activity? = LocalActivity.current,
) {

  var activityResultPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGrant ->
    if (isGrant) onRefreshVideo()
  }

  var activityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    onGrant()
  }

  Box(
    modifier = Modifier
      .fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = message,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
      )
      TextButton(
        onClick = {
          if (context.shouldShowPermissionRationale(permission, activity)) {
            context.openSetting(activityResult)
          } else if (context.shouldShowPermissionRationale(permission, activity) == false)
            activityResultPermission.launch(permission)
        },
        border = BorderStroke(
          width = 1.dp,
          color = MaterialTheme.colorScheme.onPrimary,
        ),
      ) {
        Text("Grant", color = MaterialTheme.colorScheme.onPrimary)
      }
    }
  }

}
