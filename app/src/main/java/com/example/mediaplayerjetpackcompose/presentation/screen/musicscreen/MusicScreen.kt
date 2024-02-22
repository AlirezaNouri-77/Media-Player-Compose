package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
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
import com.example.mediaplayerjetpackcompose.Constant
import com.example.mediaplayerjetpackcompose.presentation.screen.component.ArtistAlbumPage
import com.example.mediaplayerjetpackcompose.presentation.screen.component.CollapsePlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.component.FolderStyleScreen
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.MusicMediaItem
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.TopBarMusic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
  musicPageViewModel: MusicPageViewModel,
  navController: NavHostController = rememberNavController(),
  scope: CoroutineScope = rememberCoroutineScope(),
  density: Density = LocalDensity.current
) {

  Log.d("TAG7767", "MusicScreen: " + musicPageViewModel.isLoading.value)

  var showSortBar by remember { mutableStateOf(false) }
  var showSearch by remember { mutableStateOf(false) }
  val pagerState = rememberPagerState(
    initialPage = 0,
    pageCount = { Constant.tabBarListItem.size },
  )

  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value
  navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(initialValue = "Home").value
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
                onTabBarClick = {
                  scope.launch {
                    pagerState.animateScrollToPage(it)
                  }
                },
              )
            },
          ) { paddingValue ->

            if (musicPageViewModel.isLoading.value) {
              Box(
                modifier = Modifier
                  .padding(paddingValue)
                  .fillMaxSize(),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "Loading",
                  fontWeight = FontWeight.Bold,
                  fontSize = 22.sp,
                  color = MaterialTheme.colorScheme.onPrimary.copy(alpha=0.5f)
                )
              }
            } else {
              HorizontalPager(
                modifier = Modifier.padding(paddingValue),
                state = pagerState,
                userScrollEnabled = false,
              ) {
                when (it) {
                  0 -> {
                    LazyColumn( contentPadding = PaddingValues(bottom = bottomPadding.value)) {
                      itemsIndexed(
                        items = musicPageViewModel.musicList,
                        key = { _, item -> item.musicId },
                      ) { index, item ->

                        MusicMediaItem(
                          item = item,
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
                  }

                  1 -> {
                    FolderStyleScreen(
                      dataList = musicPageViewModel.artistsMusicMap,
                      onItemClick = { string ->
                        navController.navigate("Category/$string")
                      })
                  }

                  2 -> {
                    FolderStyleScreen(
                      dataList = musicPageViewModel.albumMusicMap,
                      onItemClick = { string ->
                        navController.navigate("Category/$string")
                      })
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
          ArtistAlbumPage(
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
      CollapsePlayer(
        currentMediaCurrentState = currentMusicState,
        artworkImage = artworkImage.asImageBitmap(),
        modifier = Modifier
          .onGloballyPositioned { bottomPadding.value = density.run { it.size.height.toDp() } },
        onPauseMusic = musicPageViewModel::pauseMusic,
        currentMusicPosition = currentMusicPosition,
        onResumeMusic = musicPageViewModel::resumeMusic,
        onClick = { musicPageViewModel.showBottomSheet.value = true },
      )
    }

  }

}
