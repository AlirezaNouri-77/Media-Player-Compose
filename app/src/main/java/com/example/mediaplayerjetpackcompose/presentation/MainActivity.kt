package com.example.mediaplayerjetpackcompose.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.mediaplayerjetpackcompose.data.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.presentation.screen.mediascreen.MainScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.NoPermissionPage
import com.example.mediaplayerjetpackcompose.presentation.screen.playerscreen.PlayerScreen
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

class MainActivity : ComponentActivity() {
  
  private val uiState = mutableStateOf(PermissionState.Initial)
  
  @RequiresApi(Build.VERSION_CODES.R)
  override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContent {
	  MediaPlayerJetpackComposeTheme {
		
		hideStatusBar(this)
		
		if (checkPermission(this)) {
		  uiState.value = PermissionState.PermissionIsGrant
		}
		
		PermissionHandler(
		  context = this,
		  onGrant = { uiState.value = PermissionState.PermissionIsGrant },
		  notGrant = { uiState.value = PermissionState.PermissionNotGrant },
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
				val displayName = mIntent.data!!.path
				PlayerScreen(
				  videoUri = videoUri,
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
fun PermissionHandler(context: Context, onGrant: () -> Unit, notGrant: () -> Unit) {
  val requestPermission = rememberLauncherForActivityResult(
	ActivityResultContracts.RequestPermission()
  ) {
	if (it) {
	  onGrant.invoke()
	} else {
	  notGrant.invoke()
	}
  }
  
  LaunchedEffect(!checkPermission(context)) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
	  requestPermission.launch(Manifest.permission.READ_MEDIA_VIDEO)
	} else {
	  requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
	}
  }
  
}

fun checkPermission(context: Context): Boolean {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
	ContextCompat.checkSelfPermission(
	  context,
	  Manifest.permission.READ_MEDIA_VIDEO
	)
  } else {
	Manifest.permission.READ_EXTERNAL_STORAGE
  } == PackageManager.PERMISSION_GRANTED
}

fun hideStatusBar(context: Context) {
  val window = (context as Activity).window
  WindowCompat.setDecorFitsSystemWindows(window, false)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
	window.insetsController?.apply {
	  hide(WindowInsets.Type.statusBars())
	  this.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
	}
  }
}


enum class PermissionState {
  Initial, PermissionNotGrant, PermissionIsGrant
}