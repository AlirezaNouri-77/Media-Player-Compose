package com.example.mediaplayerjetpackcompose.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.example.mediaplayerjetpackcompose.presentation.screen.MainScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.NoPermissionPage
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	
	val state = mutableStateOf(PermissionState.Initial)
	
	setContent {
	  MediaPlayerJetpackComposeTheme {
		
		if (checkPermission(this)) {
		  state.value = PermissionState.PermissionIsGrant
		}
		
		PermissionHandler(
		  context = this,
		  onGrant = { state.value = PermissionState.PermissionIsGrant },
		  notGrant = { state.value = PermissionState.PermissionNotGrant },
		)
		
		when (state.value) {
		  PermissionState.PermissionNotGrant -> {
			NoPermissionPage(onGrant = {
			  state.value = PermissionState.PermissionIsGrant
			})
		  }
		  
		  PermissionState.PermissionIsGrant -> {
			MainScreen()
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

private enum class PermissionState {
  Initial, PermissionNotGrant, PermissionIsGrant
}