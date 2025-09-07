package com.example.feature.music_player.model

import com.example.core.common.util.MusicThumbnailUtil
import com.example.core.model.MusicModel
import com.example.core.model.PlayerStateModel

data class PlayerUiState(
    val thumbnailDominantColor: Int = MusicThumbnailUtil.DEFAULT_COLOR_PALETTE,
    val currentPlayerState: PlayerStateModel = PlayerStateModel.Initial,
    val thumbnailsList: List<MusicModel> = emptyList(),
    val currentThumbnailPagerIndex: Int = 0,
    val currentPlayerPosition: Long = 0,
    val currentDeviceVolume: Int = 0,
)
