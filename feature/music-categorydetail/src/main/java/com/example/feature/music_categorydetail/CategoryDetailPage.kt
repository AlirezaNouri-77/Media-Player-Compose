package com.example.feature.music_categorydetail

import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.core.designsystem.LocalMiniPlayerHeight
import com.example.core.designsystem.LocalParentScaffoldPadding
import com.example.core.designsystem.MusicMediaItem
import com.example.core.designsystem.MusicThumbnail
import com.example.core.model.MusicModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailRoute(
    categoryViewModel: CategoryViewModel,
    categoryName: String,
    currentMusicId: String,
    isPlayerPlaying: Boolean,
    onMusicClick: (index: Int, list: List<MusicModel>) -> Unit,
    onBackClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
) {
    val uiState by categoryViewModel.uiState.collectAsStateWithLifecycle()

    CategoryDetailPage(
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState("bound"),
            animatedVisibilityScope = animatedVisibilityScope,
            renderInOverlayDuringTransition = false,
            exit = fadeOut(tween(150, 20)),
            enter = fadeIn(tween(150, 150, easing = LinearEasing)),
        ),
        uiState = uiState,
        categoryName = categoryName,
        currentMusicId = currentMusicId,
        isPlayerPlaying = isPlayerPlaying,
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
    currentMusicId: String,
    isPlayerPlaying: Boolean,
    onMusicClick: (index: Int) -> Unit,
    onBackClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
) {
    val animatedColor by animateColorAsState(
        targetValue = Color(uiState.gradientColor),
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
    )
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val isCompactLayout = remember(windowSize.windowWidthSizeClass) {
        windowSize.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    }

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
                                    0.3f to Color.Black.copy(alpha = 0.5f),
                                    1f to Color.Transparent,
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
            if (isCompactLayout) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "",
                                modifier = Modifier.size(35.dp),
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

        if (isCompactLayout) {
            CategoryDetailCompact(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                uiState = uiState,
                categoryName = categoryName,
                currentMusicId = currentMusicId,
                isPlayerPlaying = isPlayerPlaying,
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
                currentMusicId = currentMusicId,
                isPlayerPlaying = isPlayerPlaying,
                onMusicClick = onMusicClick,
                animatedVisibilityScope = animatedVisibilityScope,
                displayWithVisuals = displayWithVisuals,
                onBackClick = onBackClick,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetail(
    modifier: Modifier = Modifier,
    uiState: CategoryUiState,
    categoryName: String,
    currentMusicId: String,
    isPlayerPlaying: Boolean,
    onMusicClick: (index: Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxSize().padding(4.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "",
                        modifier = Modifier.size(35.dp),
                    )
                }
                if (displayWithVisuals) {
                    MusicThumbnail(
                        uri = uiState.songList.firstOrNull { it.artworkUri.isNotEmpty() }?.artworkUri?.toUri()
                            ?: Uri.EMPTY,
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp)),
                    )
                }
            }
            Text(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp)
                    .basicMarquee(),
                text = categoryName,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = uiState.detail,
                fontSize = 16.sp,
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(
                top = 10.dp,
                bottom = LocalParentScaffoldPadding.current.calculateBottomPadding() + if (currentMusicId.isNotEmpty()) LocalMiniPlayerHeight.current else 0.dp,
            ),
        ) {
            itemsIndexed(
                items = uiState.songList,
                key = { _, item -> item.musicId },
            ) { index, item ->
                MusicMediaItem(
                    item = item,
                    currentMediaId = currentMusicId,
                    onItemClick = {
                        onMusicClick.invoke(index)
                    },
                    isPlaying = isPlayerPlaying,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CategoryDetailCompact(
    modifier: Modifier = Modifier,
    uiState: CategoryUiState,
    categoryName: String,
    currentMusicId: String,
    isPlayerPlaying: Boolean,
    onMusicClick: (index: Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    displayWithVisuals: Boolean = true,
) {
    var currentImageSize by remember {
        mutableStateOf(240.dp)
    }

    val thumbNailNestedConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val delta = available.y.toInt()
                val newSize = currentImageSize.value + delta
                val pre = currentImageSize.value
                currentImageSize = newSize.coerceIn(160f, 240f).dp
                return Offset(x = 0f, y = currentImageSize.value - pre)
            }
        }
    }

    if (uiState.songList.isNotEmpty()) {
        Column(
            modifier = modifier.then(
                if (displayWithVisuals) {
                    Modifier.nestedScroll(thumbNailNestedConnection)
                } else {
                    Modifier
                },
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (displayWithVisuals) {
                MusicThumbnail(
                    uri = uiState.songList.firstOrNull { it.artworkUri.isNotEmpty() }?.artworkUri?.toUri()
                        ?: Uri.EMPTY,
                    modifier = Modifier
                        .size(currentImageSize)
                        .clip(RoundedCornerShape(15.dp)),
                )
            }
            Text(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        renderInOverlayDuringTransition = false,
                    )
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp)
                    .basicMarquee(),
                text = categoryName,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = uiState.detail,
                fontSize = 16.sp,
            )
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 10.dp,
                    bottom = LocalParentScaffoldPadding.current.calculateBottomPadding() + if (currentMusicId.isNotEmpty()) LocalMiniPlayerHeight.current else 0.dp,
                ),
            ) {
                itemsIndexed(
                    items = uiState.songList,
                    key = { _, item -> item.musicId },
                ) { index, item ->
                    MusicMediaItem(
                        item = item,
                        currentMediaId = currentMusicId,
                        onItemClick = {
                            onMusicClick.invoke(index)
                        },
                        isPlaying = isPlayerPlaying,
                    )
                }
            }
        }
    }
}
