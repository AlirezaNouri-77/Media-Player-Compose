package com.example.mediaplayerjetpackcompose.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.video.VideoPage
import com.example.feature.video.VideoPlayer
import com.example.feature.video.VideoViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainVideoScreen(
    modifier: Modifier = Modifier,
    videoViewModel: VideoViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val navController = rememberNavController()
    val videoUiState by videoViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = VideoScreenRoutes.VideoPage,
    ) {
        composable<VideoScreenRoutes.VideoPage> {
            VideoPage(
                modifier = modifier,
                videoUiState = videoUiState,
                onRefreshVideoList = videoViewModel::getVideo,
                onPlay = { index, list ->
                    navController.navigate(VideoScreenRoutes.VideoPlayer)
                    videoViewModel.startPlay(index, list)
                },
                onBack = onBack,
            )
        }

        composable<VideoScreenRoutes.VideoPlayer> {
            VideoPlayer(
                videoUri = "",
                videoViewModel = videoViewModel,
                onBack = navController::popBackStack,
            )
        }
    }
}

sealed interface VideoScreenRoutes {
    @Serializable
    data object VideoPage : VideoScreenRoutes

    @Serializable
    data object VideoPlayer : VideoScreenRoutes
}
