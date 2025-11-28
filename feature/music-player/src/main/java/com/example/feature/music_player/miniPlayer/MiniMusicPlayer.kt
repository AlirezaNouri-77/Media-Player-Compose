package com.example.feature.music_player.miniPlayer

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.MiniPlayerHeight
import com.example.core.designsystem.MusicThumbnail
import com.example.core.designsystem.NoRippleEffect
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.MusicModel
import com.example.feature.music_player.PagerHandler
import com.example.feature.music_player.PlayerActions
import com.example.feature.music_player.miniPlayer.component.MiniPlayerActions
import com.example.feature.music_player.miniPlayer.component.MiniPlayerPager
import com.example.feature.music_player.miniPlayer.component.miniPlayerTimeLine
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MiniMusicPlayer(
    modifier: Modifier,
    onClick: () -> Unit,
    artworkPagerList: ImmutableList<MusicModel>,
    setCurrentPagerNumber: (Int) -> Unit,
    currentPagerPage: Int,
    currentPlayerMediaId: Long,
    currentPlayerDuration: Int,
    currentPlayerArtworkUri: Uri?,
    isPlaying: Boolean,
    musicArtWorkColorAnimation: Color,
    currentMusicPosition: Long,
    onPlayerAction: (action: PlayerActions) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = currentPagerPage,
        pageCount = { artworkPagerList.size },
    )

    PagerHandler(
        currentPlayerMediaId = currentPlayerMediaId,
        pagerMusicList = artworkPagerList,
        currentPagerPage = currentPagerPage,
        pagerState = pagerState,
        setCurrentPagerNumber = setCurrentPagerNumber,
        onMoveToIndex = { onPlayerAction(PlayerActions.OnMoveToIndex(it)) },
    )

    Card(
        modifier = modifier
            .height(MiniPlayerHeight)
            .drawWithCache {
                onDrawBehind {
                    drawRoundRect(
                        color = Color.Black,
                        cornerRadius = CornerRadius(x = 25f, y = 25f),
                    )
                    drawRoundRect(
                        color = musicArtWorkColorAnimation,
                        cornerRadius = CornerRadius(x = 25f, y = 25f),
                        alpha = 0.6f,
                    )
                }
            },
        shape = RoundedCornerShape(0.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        interactionSource = NoRippleEffect,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .miniPlayerTimeLine(
                    currentMusicPosition = currentMusicPosition,
                    currentPlayerDuration = currentPlayerDuration,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MusicThumbnail(
                modifier = Modifier
                    .weight(0.2f, false)
                    .size(45.dp)
                    .clip(RoundedCornerShape(5.dp)),
                uri = currentPlayerArtworkUri,
            )
            MiniPlayerPager(
                modifier = Modifier.weight(0.7f),
                pagerState = pagerState,
                artworkList = artworkPagerList,
            )
            MiniPlayerActions(
                modifier = Modifier.weight(0.1f),
                onClickPlayAndPause = {
                    when (isPlaying) {
                        true -> onPlayerAction(PlayerActions.PausePlayer)
                        false -> onPlayerAction(PlayerActions.ResumePlayer)
                    }
                },
                isPlaying = isPlaying,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview() {
    MediaPlayerJetpackComposeTheme {
        MiniMusicPlayer(
            onClick = {},
            artworkPagerList = listOf(MusicModel.Dummy).toImmutableList(),
            setCurrentPagerNumber = {},
            currentPagerPage = 0,
            currentMusicPosition = 2000,
            onPlayerAction = {},
            modifier = Modifier
                .height(70.dp)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            currentPlayerMediaId = 0L,
            currentPlayerDuration = 207726,
            currentPlayerArtworkUri = Uri.EMPTY,
            isPlaying = true,
            musicArtWorkColorAnimation = Color.Red,
        )
    }
}
