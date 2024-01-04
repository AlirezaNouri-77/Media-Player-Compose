package com.example.mediaplayerjetpackcompose.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.data.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.presentation.screen.mediascreen.MediaPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.MediaItem

@Composable
fun MediaScreen(
  navHostController: NavHostController
) {
  
  val mediaScreenViewModel: MediaPageViewModel = viewModel(factory = MediaPageViewModel.Factory)
  
  Box(modifier = Modifier.fillMaxSize()) {
	LazyColumn(modifier = Modifier.fillMaxSize()) {
	  items(items = mediaScreenViewModel.mediaStoreDataList) {
		MediaItem(
		  item = it,
		  onItemClick = { item ->
			val videoUri = item.uri.toString().encodeStringNavigation()
			navHostController.navigate("PlayerScreen/${videoUri}"){
			  launchSingleTop = true
			}
		  },
		)
	  }
	}
  }
  
}