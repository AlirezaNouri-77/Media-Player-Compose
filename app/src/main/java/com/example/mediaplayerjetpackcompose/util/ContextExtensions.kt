package com.example.mediaplayerjetpackcompose.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat

fun Context.isPermissionGrant(permission: String) = this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun Context.isPermissionDenied(permission: String) = this.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED

fun Context.shouldShowPermissionRationale(permission: String, context: Activity?): Boolean =
  context?.let { mContext -> ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission) } == true

fun Context.openSetting(
  launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
  Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package", this.packageName, null)
  ).also { intent ->
    launcher.launch(intent)
  }
}