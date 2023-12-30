package com.example.mediaplayerjetpackcompose.presentation.screen

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.data.encodeStringNavigation
import com.example.mediaplayerjetpackcompose.presentation.MediaStoreViewModel
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.MediaItem
import java.util.Base64

@Composable
fun MediaScreen(
  navHostController: NavHostController
) {
  
  val mediaScreenViewModel: MediaStoreViewModel =
	viewModel(factory = MediaStoreViewModel.Factory, key = "mediaScreenViewModel")
  
  Box(modifier = Modifier.fillMaxSize()) {
	LazyColumn(modifier = Modifier.fillMaxSize()) {
	  items(items = mediaScreenViewModel.mediaStoreDataList) {
		MediaItem(
		  item = it,
		  onItemClick = { item ->
			val videoUri = item.uri.toString().encodeStringNavigation()
			navHostController.navigate("PlayerScreen/${videoUri}")
		  },
		)
	  }
	}
  }
  
}