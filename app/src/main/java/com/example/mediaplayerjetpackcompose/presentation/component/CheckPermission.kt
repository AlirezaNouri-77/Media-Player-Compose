package com.example.mediaplayerjetpackcompose.presentation.component

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun CheckPermission(
    permission: String,
    shouldShowPermissionRationale: () -> Unit,
    onGrant: () -> Unit,
    onDenied: () -> Unit,
    context: Context = LocalContext.current,
    activity: Activity? = LocalActivity.current,
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

        activity?.let { mContext -> ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission) } == true -> {
            shouldShowPermissionRationale()
        }

        activity?.let { mContext -> ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission) } == false -> {
            SideEffect { activityResult.launch(permission) }
        }

        else -> SideEffect { activityResult.launch(permission) }
    }
}
