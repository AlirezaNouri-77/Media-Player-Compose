package com.example.mediaplayerjetpackcompose.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.data.MusicPlayerService
import com.example.mediaplayerjetpackcompose.data.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.presentation.screen.NoPermissionPage
import com.example.mediaplayerjetpackcompose.presentation.screen.MainScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.video.player.PlayerScreen
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

class MainActivity : ComponentActivity() {
  
  var app = ApplicationClass()
  override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	
	val uiState = mutableStateOf(PermissionState.Initial)
	
	val permissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
	  arrayOf(
		Manifest.permission.READ_MEDIA_VIDEO,
		Manifest.permission.READ_MEDIA_AUDIO,
	  )
	} else {
	  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
	}
	
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
  
  override fun onStop() {
	super.onStop()
	//app.playBackHandler.release()
  }
  
  override fun onStart() {
	super.onStart()
	//app.playBackHandler.initialExoPlayer()
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
	ContextCompat.checkSelfPermission(
	  context,
	  Manifest.permission.READ_MEDIA_VIDEO
	)
  } else {
	ContextCompat.checkSelfPermission(
	  context,
	  Manifest.permission.READ_EXTERNAL_STORAGE
	)
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

