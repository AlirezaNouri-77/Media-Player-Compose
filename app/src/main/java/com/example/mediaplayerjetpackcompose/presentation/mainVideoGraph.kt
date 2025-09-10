package com.example.mediaplayerjetpackcompose.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.feature.video.VideoPage
import com.example.feature.video.VideoPlayer
import com.example.feature.video.VideoViewModel
import com.example.mediaplayerjetpackcompose.presentation.navigation.VideoTopLevelDestination
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.mainVideoGraph(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    navController: NavHostController,
) {
    navigation<VideoTopLevelDestination.Parent>(
        startDestination = VideoTopLevelDestination.VideoPage,
    ) {
        composable<VideoTopLevelDestination.VideoPage> {
            val videoViewModel: VideoViewModel = koinViewModel()
            val uiState by videoViewModel.uiState.collectAsStateWithLifecycle()
            VideoPage(
                modifier = modifier,
                videoUiState = uiState,
                onRefreshVideoList = videoViewModel::getVideo,
                onPlay = { index, list ->
                    navController.navigate(VideoTopLevelDestination.VideoPlayer)
                    videoViewModel.startPlay(index, list)
                },
                onBack = onBack,
            )
        }

        composable<VideoTopLevelDestination.VideoPlayer> {
            val videoViewModel: VideoViewModel = koinViewModel()
            VideoPlayer(
                videoViewModel = videoViewModel,
                onBack = navController::popBackStack,
            )
        }
    }
}
