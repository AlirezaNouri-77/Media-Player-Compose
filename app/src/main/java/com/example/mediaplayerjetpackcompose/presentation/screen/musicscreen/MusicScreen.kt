package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.Constant
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.ArtistAlbumPage
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.CollapsePlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.FolderStyleScreen
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
) {

  var showSortBar by remember { mutableStateOf(false) }
  val pagerState = rememberPagerState(
    initialPage = 0,
    pageCount = { Constant.tabBarListItem.size },
  )
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value
  navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(initialValue = "Home").value
  val currentMusicPosition =
    musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle()
  val artworkImage = remember(currentMusicState) {
    musicPageViewModel.getImageArt(
      uri = currentMusicState.metadata.artworkUri ?: Uri.EMPTY,
    )
  }

  LaunchedEffect(key1 = showSortBar, block = {
    delay(15000L)
    showSortBar = false
  })

  Column(
    modifier = Modifier.fillMaxSize(),
  ) {

    NavHost(
      modifier = Modifier.weight(9f, true),
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
              onSortIconClick = {
                showSortBar = !showSortBar
              },
              onTabBarClick = {
                scope.launch {
                  pagerState.animateScrollToPage(it)
                }
              },
            )
          },
        ) { paddingValue ->

          HorizontalPager(
            modifier = Modifier.padding(paddingValue),
            state = pagerState,
            beyondBoundsPageCount = Constant.tabBarListItem.size,
          ) {
            when (it) {
              0 -> {
                LazyColumn {
                  itemsIndexed(
                    items = musicPageViewModel.musicList,
                    key = { _, item -> item.musicId },
                  ) { index, item ->
                    MusicMediaItem(
                      item = item,
                      currentMediaId = currentMusicState.mediaId,
                      artworkImage = musicPageViewModel.getImageArt(item.uri),
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

    CollapsePlayer(
      currentMusicState = currentMusicState,
      onClick = { musicPageViewModel.showBottomSheet.value = true },
      artworkImage = artworkImage,
      modifier = Modifier.weight(1f),
      onPauseMusic = musicPageViewModel::pauseMusic,
      currentMusicPosition = currentMusicPosition,
      onResumeMusic = musicPageViewModel::resumeMusic,
    )

  }

}
