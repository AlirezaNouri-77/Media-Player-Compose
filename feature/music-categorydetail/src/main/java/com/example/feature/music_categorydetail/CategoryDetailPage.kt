package com.example.feature.music_categorydetail

import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.LocalBottomPadding
import com.example.core.designsystem.LocalSharedTransitionScope
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.MusicThumbnail
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CategoryDetailRoute(
    categoryViewModel: CategoryViewModel,
    categoryName: String,
    currentMusicId: String,
    isPlayerPlaying: () -> Boolean,
    onMusicClick: (index: Int, list: List<MusicModel>) -> Unit,
    onBackClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val uiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

    with(LocalSharedTransitionScope.current) {
        CategoryDetailPage(
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState("bound"),
                animatedVisibilityScope = animatedVisibilityScope,
                renderInOverlayDuringTransition = false,
                exit = fadeOut(tween(150, 20)),
                enter = fadeIn(tween(150, 150, easing = LinearEasing)),
            ),
            categoryName = categoryName,
            listItem = uiState.songList.toImmutableList(),
            lazyListBottomPadding = LocalBottomPadding.current.calculateBottomPadding(),
            currentMusicId = currentMusicId,
            isPlayerPlaying = { isPlayerPlaying() },
            onMusicClick = { onMusicClick(it, uiState.songList) },
            onBackClick = onBackClick,
            animatedVisibilityScope = animatedVisibilityScope,
            color = uiState.artWorkColor,
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CategoryDetailPage(
    modifier: Modifier = Modifier,
    categoryName: String,
    listItem: ImmutableList<MusicModel>,
    currentMusicId: String,
    isPlayerPlaying: () -> Boolean,
    onMusicClick: (index: Int) -> Unit,
    lazyListBottomPadding: Dp,
    onBackClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    color: Int,
) {
    val animate by animateColorAsState(Color(color))

    with(LocalSharedTransitionScope.current) {
        Scaffold(
            modifier = modifier.drawWithCache {
                onDrawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to animate.copy(alpha = 0.8f),
                            0.6f to animate.copy(alpha = 0.3f),
                            1f to Color.Transparent,
                            startY = 0f,
                            endY = 220.dp.toPx()
                        ),
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                                .basicMarquee(),
                            text = categoryName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBackClick.invoke() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(35.dp),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            },
            containerColor = Color.Transparent,
        ) { innerPadding ->

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MusicThumbnail(
                    modifier = Modifier.Companion
                        .size(size = 220.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(5.dp))
                        .background(color = MaterialTheme.colorScheme.primary),
                    uri = listItem.firstOrNull { it.artworkUri.isNotEmpty() }?.artworkUri?.toUri() ?: Uri.EMPTY,
                )
                LazyColumn(
                    contentPadding = PaddingValues(top = 10.dp, bottom = lazyListBottomPadding)
                ) {
                    itemsIndexed(
                        items = listItem,
                        key = { _, item -> item.musicId },
                    ) { index, item ->
                        MusicMediaItem(
                            item = item,
                            currentMediaId = currentMusicId,
                            onItemClick = {
                                onMusicClick.invoke(index)
                            },
                            isPlaying = { isPlayerPlaying() },
                        )
                    }
                }
            }

        }
    }
}