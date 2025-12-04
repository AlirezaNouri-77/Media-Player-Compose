package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.feature.video.VideoPage
import com.example.feature.video.VideoPlayer
import com.example.feature.video.VideoViewModel
import com.example.mediaplayerjetpackcompose.presentation.MusicMain
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavMainGraph() {
    val backStack = retain { BackStackHandler<NavKey>(Music) }

    NavDisplay(
        backStack = backStack.backStack,
        onBack = { backStack.removeLast() },
        entryProvider = entryProvider {
            entry<Music> {
                MusicMain(
                    navigateToVideo = {
                        backStack.addTopLevel(VideoHome, true)
                    },
                )
            }
            entry<VideoHome> {
                val videoViewModel: VideoViewModel = koinViewModel()
                val uiState by videoViewModel.uiState.collectAsStateWithLifecycle()
                VideoPage(
                    videoUiState = uiState,
                    onRefreshVideoList = videoViewModel::getVideo,
                    onPlay = { index, list ->
                        backStack.add(VideoPlayer)
                        videoViewModel.startPlay(index, list)
                    },
                    onBack = {
                        backStack.addTopLevel(Music, true)
                    },
                )
            }
            entry<VideoPlayer> {
                val videoViewModel: VideoViewModel = koinViewModel()
                VideoPlayer(
                    videoViewModel = videoViewModel,
                    onBack = {
                        backStack.addTopLevel(VideoHome, true)
                    },
                )
            }
        },
    )
}
