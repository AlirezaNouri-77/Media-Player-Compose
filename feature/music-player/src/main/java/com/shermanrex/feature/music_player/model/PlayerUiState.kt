package com.shermanrex.feature.music_player.model

import androidx.compose.runtime.Stable
import com.shermanrex.core.data.util.MusicThumbnailUtil
import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.MusicPlayerState
import com.shermanrex.core.model.PlayerTimerState
import com.shermanrex.core.music_media3.model.ArtworkModel

@Stable
data class PlayerUiState(
    val thumbnailDominantColor: Int = MusicThumbnailUtil.DEFAULT_COLOR_PALETTE,
    val currentPlayerState: MusicPlayerState = MusicPlayerState.Initial,
    val thumbnailsList: List<ArtworkModel> = emptyList(),
    val playedMusicList: List<MusicModel> = emptyList(),
    val currentThumbnailPagerIndex: Int = 0,
    val currentPlayerPosition: Long = 0,
    val currentDeviceVolume: Int = 0,
    val shouldShowTimerBottomSheet: Boolean = false,
    val playerTimerState: PlayerTimerState = PlayerTimerState.Initial,
)
