package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.domain.model.TabBarPosition
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.CategoryPage
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.MiniMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.MusicMediaItem
import com.example.mediaplayerjetpackcompose.presentation.screen.component.topbar.TopBarMusic
import com.example.mediaplayerjetpackcompose.presentation.screen.music.item.CategoryListItem
import kotlinx.coroutines.delay

@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  density: Density = LocalDensity.current
) {

  var showSortBar by remember { mutableStateOf(false) }
  var showSearch by remember { mutableStateOf(false) }

  val navController: NavHostController = rememberNavController()
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value
  val currentMusicPosition =
    musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle(0)
  val artworkImage = remember(currentMusicState) {
    musicPageViewModel.getImageArt(
      uri = currentMusicState.metaData.artworkUri ?: Uri.EMPTY,
    )
  }
  val bottomPadding = remember {
    mutableStateOf(0.dp)
  }

  LaunchedEffect(key1 = showSortBar, block = {
    delay(15000L)
    showSortBar = false
  })

  ConstraintLayout(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    val (musicList, collapsePlayer) = createRefs()

    Box(
      modifier = Modifier.constrainAs(musicList) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom)
      },
    ) {
      NavHost(
        navController = navController,
        startDestination = "Home",
      ) {
        composable("Home") {
          Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
              TopBarMusic(
                musicPageViewModel = musicPageViewModel,
                showSortBar = showSortBar,
                showSearch = showSearch,
                currentTabPosition = musicPageViewModel.currentTabState,
                onSortIconClick = { showSortBar = !showSortBar },
                onSearchIconClick = { showSearch = !showSearch },
              )
            },
          ) { paddingValue ->

            if (musicPageViewModel.isLoading) {
              Box(
                Modifier
                  .padding(paddingValue)
                  .fillMaxSize(),
                contentAlignment = Alignment.Center
              ) {
                Text(text = "Loading")
              }
            } else {
              MusicList(
                musicPageViewModel = musicPageViewModel,
                currentMusicState = currentMusicState,
                navController = navController,
                paddingValue = paddingValue,
                bottomPadding = bottomPadding.value,
              )
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
          CategoryPage(
            name = it.arguments!!.getString("CategoryName").toString(),
            currentMediaCurrentState = currentMusicState,
            musicPageViewModel = musicPageViewModel,
            onMusicClick = { index, musicList ->
              musicPageViewModel.playMusic(
                index = index,
                musicList
              )
            },
            onBackClick = navController::popBackStack
          )
        }

      }
    }


    AnimatedVisibility(
      visible = currentMusicState.mediaId.isNotEmpty(),
      modifier = Modifier
        .constrainAs(collapsePlayer) {
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          bottom.linkTo(parent.bottom)
        },
      enter = fadeIn(tween(100)) + slideInVertically(
        animationSpec = tween(400),
        initialOffsetY = { int -> int / 4 }),
      exit = slideOutVertically(
        animationSpec = tween(400, 100),
        targetOffsetY = { int -> int / 4 }) + fadeOut(tween(100))
    ) {
      MiniMusicPlayer(
        currentMediaCurrentState = currentMusicState,
        artworkImage = artworkImage.asImageBitmap(),
        modifier = Modifier
          .onGloballyPositioned { bottomPadding.value = density.run { it.size.height.toDp() } },
        onPauseMusic = musicPageViewModel::pauseMusic,
        currentMusicPosition = currentMusicPosition,
        onResumeMusic = musicPageViewModel::resumeMusic,
        onNextMusic = { musicPageViewModel.moveToNext() },
        onPreviewMusic = { musicPageViewModel.moveToPrevious() },
        onClick = { musicPageViewModel.showBottomSheet.value = true },
      )
    }

  }
}

@Composable
fun MusicList(
  musicPageViewModel: MusicPageViewModel,
  currentMusicState: MediaCurrentState,
  navController: NavController,
  paddingValue: PaddingValues,
  bottomPadding: Dp,
) {
  AnimatedContent(
    targetState = musicPageViewModel.currentTabState,
    label = "",
    transitionSpec = {
      fadeIn(tween(250)).togetherWith(fadeOut(tween(200)))
    }
  ) {

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValue),
      contentPadding = PaddingValues(bottom = bottomPadding),
    ) {
      when (it) {
        TabBarPosition.MUSIC -> {
          itemsIndexed(
            items = musicPageViewModel.musicList,
            key = { _, item -> item.musicId },
          ) { index, item ->
            MusicMediaItem(
              item = item,
              isFav = item.musicId.toString() in musicPageViewModel.favoriteListMediaId.toList(),
              currentMediaId = currentMusicState.mediaId,
              artworkImage = item.artBitmap.asImageBitmap(),
              onItemClick = {
                musicPageViewModel.playMusic(
                  index = index,
                  musicPageViewModel.musicList
                )
              },
            )
          }
        }

        TabBarPosition.FAVORITE -> {

          musicPageViewModel.favoriteMusicList =
            musicPageViewModel.musicList.filter { it.musicId.toString() in musicPageViewModel.favoriteListMediaId }
              .toMutableStateList()

          if (musicPageViewModel.favoriteMusicList.isNotEmpty()) {
            itemsIndexed(
              items = musicPageViewModel.favoriteMusicList,
              key = { _, item -> item.musicId },
            ) { index, item ->
              MusicMediaItem(
                item = item,
                isFav = item.musicId.toString() in musicPageViewModel.favoriteListMediaId.toList(),
                currentMediaId = currentMusicState.mediaId,
                artworkImage = item.artBitmap.asImageBitmap(),
                onItemClick = {
                  musicPageViewModel.playMusic(
                    index = index,
                    musicPageViewModel.favoriteMusicList
                  )
                },
              )
            }
          }


        }

        TabBarPosition.ARTIST -> {
          items(musicPageViewModel.artistsMusicMap) { item ->
            CategoryListItem(
              categoryName = item.name,
              artWork = null,
              musicListSize = item.list.size,
              onClick = { string ->
                navController.navigate("Category/$string")
              },
            )
          }
        }

        TabBarPosition.ALBUM -> {
          items(musicPageViewModel.albumMusicMap) { item ->
            CategoryListItem(
              categoryName = item.name,
              artWork = item.list.first().artBitmap,
              musicListSize = item.list.size,
              onClick = { string ->
                navController.navigate("Category/$string")
              },
            )
          }
        }

      }
    }
  }
}