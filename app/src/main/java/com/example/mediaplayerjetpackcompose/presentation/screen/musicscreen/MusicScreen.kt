package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.MusicMediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  scope: CoroutineScope = rememberCoroutineScope(),
) {
  
  val icon = if (musicPageViewModel.isPlayerRunning) R.drawable.icon_pause else R.drawable.icon_play
  val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val showBottomSheet = remember { mutableStateOf(false) }
  val bottomSheetOffset = remember(modalBottomSheetState) {
	derivedStateOf {
	  runCatching { modalBottomSheetState.requireOffset() }.getOrDefault(0f)
	}
  }
  
  LaunchedEffect(key1 = bottomSheetOffset.value, block = {
	musicPageViewModel.shouldHideTopBarAndBottomBar = bottomSheetOffset.value <= 1000f
  })
  
  ConstraintLayout(
	modifier = Modifier
	  .fillMaxSize(),
  ) {
	
	val (list, bottomRow) = createRefs()
	
	if (showBottomSheet.value) {
	  BottomSheet(
		musicPageViewModel = musicPageViewModel,
		sheetState = modalBottomSheetState,
		icon = icon,
		onDismissed = {
		  scope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
			if (!modalBottomSheetState.isVisible) showBottomSheet.value = false
		  }
		}
	  )
	}
	
	LazyColumn(
	  modifier = Modifier
		.fillMaxSize()
		.constrainAs(ref = list) {
		  top.linkTo(parent.top)
		  bottom.linkTo(parent.bottom)
		  start.linkTo(parent.start)
		  end.linkTo(parent.end)
		},
	) {
	  itemsIndexed(
		items = musicPageViewModel.musicMediaStoreDataList,
		key = { _, item -> item.id },
	  ) { index, item ->
		MusicMediaItem(
		  item = item,
		  onItemClick = { musicMediaModel ->
			showBottomSheet.value = true
			musicPageViewModel.apply {
			  currentListPosition = index
			  currentMusic.value = musicMediaModel
			  playMusic(musicMediaModel.uri)
			}
		  },
		)
	  }
	}
	
	Row(
	  verticalAlignment = Alignment.CenterVertically,
	  horizontalArrangement = Arrangement.Center,
	  modifier = Modifier
		.fillMaxWidth()
		.padding(5.dp)
		.background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
		.clickable { showBottomSheet.value = true }
		.constrainAs(bottomRow) { bottom.linkTo(parent.bottom, margin = 6.dp) },
	) {
	  Text(
		text = musicPageViewModel.currentMusic.value.name,
		fontSize = 16.sp,
		fontWeight = FontWeight.Normal,
		modifier = Modifier
		  .fillMaxWidth()
		  .weight(1f, true)
		  .padding(horizontal = 5.dp)
		  .basicMarquee(),
		maxLines = 1,
	  )
	  Button(
		onClick = {
		  when (musicPageViewModel.isPlayerRunning) {
			true -> musicPageViewModel.musicPlayer.pause()
			false -> musicPageViewModel.musicPlayer.play()
		  }
		},
		colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
	  ) {
		AnimatedContent(targetState = icon, label = "") { int ->
		  Image(
			painter = painterResource(id = int),
			contentDescription = "",
			modifier = Modifier.size(25.dp),
		  )
		}
	  }
	}
  }
  
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
  musicPageViewModel: MusicPageViewModel,
  sheetState: SheetState,
  onDismissed: () -> Unit,
  icon: Int,
) {
  
  ModalBottomSheet(
	onDismissRequest = { onDismissed.invoke() },
	Modifier.fillMaxSize(),
	sheetState = sheetState,
	shape = RectangleShape,
  ) {
	Column(
	  modifier = Modifier.fillMaxSize(),
	  verticalArrangement = Arrangement.Center,
	  horizontalAlignment = Alignment.CenterHorizontally,
	) {
	  
	  Text(
		text = musicPageViewModel.currentMusic.value.artist,
		fontSize = 22.sp,
		fontWeight = FontWeight.SemiBold,
		modifier = Modifier.padding(15.dp),
	  )
	  
	  if (musicPageViewModel.currentMusic.value.image != null) {
		Image(
		  bitmap = musicPageViewModel.currentMusic.value.image!!.asImageBitmap(),
		  contentDescription = "",
		  modifier = Modifier
			.size(300.dp)
			.clip(RoundedCornerShape(15.dp))
		)
	  } else {
		Image(
		  painter = painterResource(id = R.drawable.placeholder_music),
		  contentDescription = "",
		  modifier = Modifier
			.size(300.dp)
			.clip(RoundedCornerShape(15.dp))
		)
	  }
	  
	  Spacer(modifier = Modifier.height(25.dp))
	  
	  Text(
		text = musicPageViewModel.currentMusic.value.name,
		fontSize = 20.sp,
		modifier = Modifier
		  .fillMaxWidth()
		  .padding(10.dp)
		  .basicMarquee(),
		fontWeight = FontWeight.Medium,
		maxLines = 1,
	  )
	  
	  Spacer(modifier = Modifier.height(15.dp))
	  
	  Row(
		modifier = Modifier
		  .fillMaxWidth()
		  .padding(horizontal = 15.dp),
		horizontalArrangement = Arrangement.Absolute.SpaceBetween,
	  ) {
		Text(
		  text = musicPageViewModel.currentMusicPosition.toInt().convertMilliSecondToTime(),
		  fontSize = 13.sp,
		  fontWeight = FontWeight.Normal,
		)
		Text(
		  text = musicPageViewModel.currentMusic.value.duration.convertMilliSecondToTime(),
		  fontSize = 13.sp,
		  fontWeight = FontWeight.Normal,
		)
	  }
	  Slider(
		value = musicPageViewModel.currentMusicPosition,
		modifier = Modifier
		  .fillMaxWidth()
		  .padding(horizontal = 15.dp),
		onValueChange = {
		  musicPageViewModel.currentMusicPosition = it
		  musicPageViewModel.musicPlayer.seekTo(it.toLong())
		},
		valueRange = 0f..musicPageViewModel.currentMusic.value.duration.toFloat(),
	  )
	  
	  Spacer(modifier = Modifier.height(15.dp))
	  
	  Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center,
	  ) {
		
		Button(
		  onClick = {
			musicPageViewModel.moveToBeforeMusic()
		  },
		  colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
		) {
		  Image(
			painter = painterResource(id = R.drawable.next),
			contentDescription = "",
			modifier = Modifier
			  .size(25.dp)
			  .rotate(180f)
		  )
		}
		
		Spacer(modifier = Modifier.width(15.dp))
		
		Button(
		  onClick = {
			when (musicPageViewModel.isPlayerRunning) {
			  true -> musicPageViewModel.musicPlayer.pause()
			  false -> musicPageViewModel.musicPlayer.play()
			}
		  },
		  colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
		) {
		  AnimatedContent(targetState = icon, label = "") {
			Image(
			  painter = painterResource(id = it),
			  contentDescription = "",
			  modifier = Modifier.size(55.dp),
			)
		  }
		}
		
		Spacer(modifier = Modifier.width(15.dp))
		
		Button(
		  onClick = {
			musicPageViewModel.moveToNextMusic()
		  },
		  colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
		) {
		  Image(
			painter = painterResource(id = R.drawable.next),
			contentDescription = "",
			modifier = Modifier.size(25.dp)
		  )
		}
		
	  }
	}
  }
}
