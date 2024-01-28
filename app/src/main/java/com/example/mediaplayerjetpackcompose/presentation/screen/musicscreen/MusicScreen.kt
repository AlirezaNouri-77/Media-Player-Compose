package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.BottomSheet
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.CollapsePlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.sortBar
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.MusicMediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  scope: CoroutineScope = rememberCoroutineScope(),
  navController: NavHostController = rememberNavController()
) {
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value
  val currentMusicPosition =
    musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle().value
  val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val showBottomSheet = remember { mutableStateOf(false) }
  val artworkImage = remember(currentMusicState) {
    musicPageViewModel.getImageArt(currentMusicState.metadata.artworkUri ?: Uri.EMPTY)
      .asImageBitmap()
  }
  navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(initialValue = "Home").value
  val currentRoute = navController.currentDestination?.route

  Column(
    modifier = Modifier.fillMaxSize(),
  ) {

    if (currentRoute == "Home") {
      Text(
        text = "Music",
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
      )
      CategoryBar(musicPageViewModel = musicPageViewModel)
    }

    if (musicPageViewModel.currentTabState == 0) {
      LazyRow(modifier = Modifier.padding(horizontal = 15.dp)) {
        sortBar(musicPageViewModel = musicPageViewModel)
      }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

      val (musicList, collapsePlayer) = createRefs()

      NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
          AnimatedContent(targetState = musicPageViewModel.currentTabState, label = "") {

            LazyColumn(
              Modifier
                .fillMaxSize()
                .constrainAs(musicList) {
                  top.linkTo(parent.top)
                  start.linkTo(parent.start)
                  end.linkTo(parent.end)
                  bottom.linkTo(parent.bottom)
                },
            ) {

              when (it) {
                0 -> {
                  itemsIndexed(
                    items = musicPageViewModel.musicList,
                    key = { _, item -> item.musicId },
                  ) { index, item ->
                    MusicMediaItem(
                      modifier = Modifier.animateItemPlacement(),
                      item = item,
                      currentMediaID = currentMusicState.mediaId,
                      onItemClick = {
                        showBottomSheet.value = true
                        musicPageViewModel.playMusic(index = index, musicPageViewModel.musicList)
                      },
                    )
                  }
                }

                1 -> {
                  musicPageViewModel.artistsMusicMap.forEach { (key, list) ->
                    item {
                      FolderStyleMusicItem(
                        name = key,
                        musicListSize = list.size,
                        onClick = {
                          navController.navigate("Category/$it")
                        },
                      )
                    }
                  }
                }

                2 -> {
                  musicPageViewModel.albumMusicMap.forEach { (key, list) ->
                    item {
                      FolderStyleMusicItem(
                        name = key,
                        musicListSize = list.size,
                        onClick = {
                          navController.navigate("Category/$it")
                        },
                      )
                    }
                  }
                }
              }

            }
          }
        }
        composable(
          "Category/{CategoryName}",
          arguments = listOf(
            navArgument(name = "CategoryName") {
              type = NavType.StringType
            },
          )
        ) {
          ArtistAlbumScreen(
            name = it.arguments!!.getString("CategoryName").toString(),
            musicState = currentMusicState,
            musicPageViewModel = musicPageViewModel,
            onMusicClick = { index, musicList ->
              musicPageViewModel.playMusic(
                index = index,
                musicList
              )
            }
          )
        }
      }

      CollapsePlayer(
        modifier = Modifier.constrainAs(collapsePlayer) {
          end.linkTo(parent.end)
          start.linkTo(parent.start)
          bottom.linkTo(parent.bottom)
        },
        currentMusicState = currentMusicState,
        onClick = { showBottomSheet.value = true },
        onPauseMusic = musicPageViewModel::pauseMusic,
        onResumeMusic = musicPageViewModel::resumeMusic,
      )

    }

    if (showBottomSheet.value) {
      BottomSheet(
        sheetState = modalBottomSheetState,
        currentMusicState = currentMusicState,
        artworkImage = artworkImage,
        currentMusicPosition = currentMusicPosition,
        onPauseMusic = musicPageViewModel::pauseMusic,
        onResumeMusic = musicPageViewModel::resumeMusic,
        onMoveNextMusic = musicPageViewModel::moveToNext,
        onMovePreviousMusic = musicPageViewModel::moveToPrevious,
        onSeekTo = { musicPageViewModel.seekToPosition(it) },
        onDismissed = {
          scope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
            if (!modalBottomSheetState.isVisible) showBottomSheet.value = false
          }
        },
      )
    }
  }

}

@Composable
fun CategoryBar(
  musicPageViewModel: MusicPageViewModel,
  tabItemList: List<String> = listOf("Music", "Artist", "Album"),
) {
  TabRow(
    selectedTabIndex = musicPageViewModel.currentTabState,
    modifier = Modifier.fillMaxWidth(),
  ) {
    tabItemList.forEachIndexed { index, string ->
      Tab(
        text = { Text(text = string) },
        selected = musicPageViewModel.currentTabState == index,
        onClick = {
          musicPageViewModel.currentTabState = index
        },
      )
    }
  }
}

// used for album and artist tabview
@Composable
fun FolderStyleMusicItem(
  name: String,
  musicListSize: Int,
  onClick: (String) -> Unit,
) {
  Surface(onClick = { onClick.invoke(name) }) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 7.dp, horizontal = 5.dp),
    ) {
      Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
      Text(text = musicListSize.toString(), fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistAlbumScreen(
  name: String,
  musicPageViewModel: MusicPageViewModel,
  musicState: MusicState,
  onMusicClick: (index: Int, musicList: List<MusicMediaModel>) -> Unit,
) {

  musicPageViewModel.musicCategoryList = remember {
    when (musicPageViewModel.currentTabState) {
      1 -> musicPageViewModel.artistsMusicMap[name]!!.toMutableStateList()
      2 -> musicPageViewModel.albumMusicMap[name]!!.toMutableStateList()
      else -> emptyList<MusicMediaModel>().toMutableStateList()
    }
  }

  Column {

    Text(
      text = name,
      fontSize = 24.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
    )

    LazyRow(modifier = Modifier.padding(horizontal = 15.dp)) {
      sortBar(musicPageViewModel = musicPageViewModel)
    }

    LazyColumn(Modifier.fillMaxSize()) {
      itemsIndexed(
        items = musicPageViewModel.musicCategoryList,
        key = { _, item -> item.musicId },
      ) { index, item ->
        MusicMediaItem(
          item = item,
          currentMediaID = musicState.mediaId,
          onItemClick = {
            onMusicClick.invoke(index, musicPageViewModel.musicCategoryList)
          },
          modifier = Modifier.animateItemPlacement(),
        )
      }
    }

  }

}