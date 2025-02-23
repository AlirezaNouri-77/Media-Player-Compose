package com.example.mediaplayerjetpackcompose.presentation.screen.video.component

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.util.Constant
import com.example.mediaplayerjetpackcompose.core.designSystem.EmptyPage
import com.example.mediaplayerjetpackcompose.util.isPermissionGrant

@Composable
fun EmptyVideoResultHandler(
  context: Context,
  onRefreshVideoList: () -> Unit
) {

  when {
    Build.VERSION.SDK_INT >= Constant.API_34_UPSIDE_DOWN_CAKE_ANDROID_14 -> {

      EmptyPage(
        message = if (context.isPermissionGrant(Manifest.permission.READ_MEDIA_VIDEO) == true || context.isPermissionGrant(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == true) {
          "Empty"
        } else {
          "You need grant permission or select videos"
        },
        textSize = 16.sp,
        textAlpha = 0.8f,
      )
    }

    Build.VERSION.SDK_INT >= Constant.API_33_TIRAMISU_ANDROID_13 && context.isPermissionGrant(Manifest.permission.READ_MEDIA_VIDEO) == false -> {
      PermissionApi33ToLowerHandler(
        context = context,
        message = "Video storage Permission isn't granted",
        permission = Manifest.permission.READ_MEDIA_VIDEO,
        onGrant = {
          onRefreshVideoList()
        },
        onRefreshVideo = {
          onRefreshVideoList()
        }
      )
    }

    Build.VERSION.SDK_INT < Constant.API_33_TIRAMISU_ANDROID_13 && context.isPermissionGrant(Manifest.permission.READ_EXTERNAL_STORAGE) == false -> {
      PermissionApi33ToLowerHandler(
        context = context,
        message = "Storage Permission isn't granted",
        permission = Manifest.permission.READ_EXTERNAL_STORAGE,
        onGrant = {
          onRefreshVideoList()
        },
        onRefreshVideo = {
          onRefreshVideoList()
        }
      )
    }

    else -> EmptyPage()

  }
}