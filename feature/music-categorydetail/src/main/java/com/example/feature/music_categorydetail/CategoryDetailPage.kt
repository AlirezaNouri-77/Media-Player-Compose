package com.example.feature.music_categorydetail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.util.CurrentWindowSizeState
import com.example.core.model.MusicModel
import com.example.core.model.WindowSize
import com.example.feature.music_categorydetail.component.CategoryDetail
import com.example.feature.music_categorydetail.component.CategoryDetailPortrait

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailRoute(
    categoryViewModel: CategoryViewModel,
    categoryName: String,
    onMusicClick: (index: Int, list: List<MusicModel>) -> Unit,
    onBackClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
) {
    val uiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

    CategoryDetailPage(
        uiState = uiState,
        categoryName = categoryName,
        onMusicClick = { onMusicClick(it, uiState.songList) },
        onBackClick = onBackClick,
        animatedVisibilityScope = animatedVisibilityScope,
        displayWithVisuals = displayWithVisuals,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailPage(
    modifier: Modifier = Modifier,
    uiState: CategoryUiState,
    categoryName: String,
    onMusicClick: (index: Int) -> Unit,
    onBackClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
) {
    val animatedColor by animateColorAsState(
        targetValue = Color(uiState.thumbnailDominateColor),
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
    )
    val windowSize = CurrentWindowSizeState()

    Scaffold(
        modifier = modifier
            .then(
                if (displayWithVisuals) {
                    Modifier.drawWithCache {
                        onDrawWithContent {
                            drawRect(
                                brush = Brush.verticalGradient(
                                    0.4f to animatedColor.copy(alpha = 0.5f),
                                    0.7f to animatedColor.copy(alpha = 0.3f),
                                    1f to Color.Transparent,
                                ),
                            )
                            drawRect(
                                Brush.verticalGradient(
                                    0.3f to Color.Transparent,
                                    1f to Color.Black.copy(0.6f),
                                    startY = this.size.height,
                                    endY = 0f,
                                ),
                            )
                            drawContent()
                        }
                    }
                } else {
                    Modifier
                },
            ),
        topBar = {
            if (windowSize == WindowSize.COMPACT) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "",
                                modifier = Modifier.size(35.dp),
                                tint = Color.White,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        },
        containerColor = Color.Transparent,
    ) { innerPadding ->

        if (windowSize == WindowSize.COMPACT) {
            CategoryDetailPortrait(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                uiState = uiState,
                categoryName = categoryName,
                currentMusicId = uiState.musicPlayerState.currentMediaInfo.musicID,
                isPlayerPlaying = uiState.musicPlayerState.isPlaying,
                onMusicClick = onMusicClick,
                animatedVisibilityScope = animatedVisibilityScope,
                displayWithVisuals = displayWithVisuals,
            )
        } else {
            CategoryDetail(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                uiState = uiState,
                categoryName = categoryName,
                currentMusicId = uiState.musicPlayerState.currentMediaInfo.musicID,
                isPlayerPlaying = uiState.musicPlayerState.isPlaying,
                onMusicClick = onMusicClick,
                animatedVisibilityScope = animatedVisibilityScope,
                displayWithVisuals = displayWithVisuals,
                onBackClick = onBackClick,
            )
        }
    }
}
