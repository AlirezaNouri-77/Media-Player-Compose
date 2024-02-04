package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
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
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.CollapsePlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.sortBar
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.MusicMediaItem
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.TopBarMusic
import kotlinx.coroutines.delay

@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  paddingValues: PaddingValues,
  navController: NavHostController = rememberNavController(),
) {

  var showSortBar by remember { mutableStateOf(false) }
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value
  navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(initialValue = "Home").value
  val currentRoute = navController.currentDestination?.route



  LaunchedEffect(key1 = showSortBar, block = {
    delay(10000L)
    showSortBar = false
  })

  NavHost(
    navController = navController,
    startDestination = "Home",
    modifier = Modifier.padding(paddingValues)
  ) {

    composable("Home") {
      AnimatedContent(targetState = musicPageViewModel.currentTabState, label = "") { animatedInt ->

        Scaffold(
          contentWindowInsets = WindowInsets(0.dp),
          topBar = {
            TopBarMusic(
              musicPageViewModel = musicPageViewModel,
              showSortBar = showSortBar,
              onSortIconClick = {
                showSortBar = !showSortBar
              },
            )
          },
        ) { paddingValue ->

          ConstraintLayout(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValue),
          ) {
            val (musicList, collapsePlayer) = createRefs()

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

              when (animatedInt) {
                0 -> {
                  itemsIndexed(
                    items = musicPageViewModel.musicList,
                    key = { _, item -> item.musicId },
                  ) { index, item ->
                    MusicMediaItem(
                      item = item,
                      currentMediaId = currentMusicState.mediaId,
                      artworkImage = musicPageViewModel.getImageArt(item.uri),
                      onItemClick = {
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

            CollapsePlayer(
              modifier = Modifier.constrainAs(collapsePlayer) {
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
              },
              currentMusicState = currentMusicState,
              onClick = { musicPageViewModel.showBottomSheet.value = true },
              onPauseMusic = musicPageViewModel::pauseMusic,
              onResumeMusic = musicPageViewModel::resumeMusic,
            )

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
        currentMusicState = currentMusicState,
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArtistAlbumScreen(
  name: String,
  musicPageViewModel: MusicPageViewModel,
  currentMusicState: MusicState,
  onMusicClick: (index: Int, musicList: List<MusicMediaModel>) -> Unit,
) {

  musicPageViewModel.musicCategoryList = remember(musicPageViewModel.currentTabState) {
    when (musicPageViewModel.currentTabState) {
      1 -> musicPageViewModel.artistsMusicMap[name]!!.toMutableStateList()
      2 -> musicPageViewModel.albumMusicMap[name]!!.toMutableStateList()
      else -> emptyList<MusicMediaModel>().toMutableStateList()
    }
  }

  Scaffold(
    topBar = {
      MediumTopAppBar(
        title = {
          Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
          )
        },
        navigationIcon = {
          Image(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "",
            modifier = Modifier.size(35.dp)
          )
        }
      )
    }
  ) {
    Column(modifier = Modifier.padding(it)) {

      LazyRow(modifier = Modifier.padding(horizontal = 15.dp)) {
        sortBar(
          musicPageViewModel = musicPageViewModel,
          onSortClick = {
            musicPageViewModel.currentListSort.value = it
            musicPageViewModel.sortMusicListByCategory(
              list = musicPageViewModel.musicCategoryList
            ).also { resultList ->
              musicPageViewModel.musicCategoryList =
                resultList as SnapshotStateList<MusicMediaModel>
            }
          },
          onDec = {
            musicPageViewModel.isDec.value = !musicPageViewModel.isDec.value
            musicPageViewModel.sortMusicListByCategory(musicPageViewModel.musicCategoryList)
              .also { resultList ->
                musicPageViewModel.musicCategoryList =
                  resultList as SnapshotStateList<MusicMediaModel>
              }
          },
        )
      }

      LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(
          items = musicPageViewModel.musicCategoryList,
          key = { _, item -> item.musicId },
        ) { index, item ->
          MusicMediaItem(
            item = item,
            currentMediaId = currentMusicState.mediaId,
            artworkImage = musicPageViewModel.getImageArt(item.uri),
            onItemClick = {
              onMusicClick.invoke(index, musicPageViewModel.musicCategoryList)
            },
          )
        }
      }

    }
  }


}

fun Modifier.myCustomTabIndicator(
  currentTapPosition: TabPosition,
): Modifier = composed {
  val currentTabWidth by animateDpAsState(
    targetValue = currentTapPosition.width,
    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing), label = "",
  )
  val currentTabIndicator by animateDpAsState(
    targetValue = currentTapPosition.left,
    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing), label = "",
  )
  wrapContentSize(CenterStart)
    .offset(x = currentTabIndicator)
    .width(currentTabWidth)
}

@Composable
fun MyNewIndicator(modifier: Modifier = Modifier) {
  Column(
    modifier
      .fillMaxSize()
      .padding(5.dp)
      .background(
        colorResource(id = R.color.purple_500).copy(alpha = 0.4f),
        RoundedCornerShape(15.dp),
      ),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
  }
}