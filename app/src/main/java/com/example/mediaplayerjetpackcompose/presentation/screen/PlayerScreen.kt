package com.example.mediaplayerjetpackcompose.presentation.screen

import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import androidx.media3.ui.TimeBar
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.PlayerViewModel
import com.example.mediaplayerjetpackcompose.R

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
  videoUri: String,
  navHostController: NavHostController,
) {
  
  val context = LocalContext.current
  val playerViewModel = viewModel<PlayerViewModel>(factory = PlayerViewModel.Factory)
  
  var currentPosition = remember {
	mutableLongStateOf(0L)
  }
  
  val scurbInprograss = remember {
	mutableStateOf(false)
  }
  
  BackHandler(enabled = true) {
	playerViewModel.onBackPress.value = true
  }
  
  LaunchedEffect(key1 = videoUri, block = {
	playerViewModel.startPlayVideo(videoUri.decodeStringNavigation())
  })
  
  DisposableEffect(
	key1 = playerViewModel.onBackPress.value,
	effect = {
	  onDispose {
		playerViewModel.stopPlayVideo()
		navHostController.popBackStack(route = NavigationRoute.MediaScreen.route, inclusive = false)
	  }
	})
  
  Box(modifier = Modifier.fillMaxSize()) {
	AndroidView(
	  modifier = Modifier
		.fillMaxSize()
		.background(color = Color.Black),
	  factory = {
		PlayerView(it).apply {
		  this.player = playerViewModel.exoPlayer
		  this.useController = true
		  this.setControllerVisibilityListener(object : PlayerView.ControllerVisibilityListener {
			override fun onVisibilityChanged(visibility: Int) {
			  when(visibility){
				View.VISIBLE->{
				  scurbInprograss.value = true
				}
				View.GONE->{
				  scurbInprograss.value = false
				}
			  }
			}
			
		  })
		}
	  },
	)
  }
  
  if (scurbInprograss.value) {
	Box(modifier = Modifier.fillMaxSize()) {
	  Box(
		modifier = Modifier
		  .fillMaxWidth(),
		contentAlignment = Alignment.TopStart,
	  ) {
		Row(
		  verticalAlignment = Alignment.CenterVertically,
		  horizontalArrangement = Arrangement.Start,
		) {
		  Image(
			painter = painterResource(id = R.drawable.back_icon),
			contentDescription = "",
			modifier = Modifier
			  .clickable {
				playerViewModel.stopPlayVideo()
				navHostController.popBackStack(
				  route = NavigationRoute.MediaScreen.route,
				  inclusive = false
				)
			  }
			  .padding(20.dp),
		  )
		  Spacer(modifier = Modifier.width(15.dp))
		  Text(
			text = "Name of Video",
			fontSize = 16.sp,
			fontWeight = FontWeight.Medium,
			color = Color.White,
		  )
		}
	  }
	}
  }
  
  
}
