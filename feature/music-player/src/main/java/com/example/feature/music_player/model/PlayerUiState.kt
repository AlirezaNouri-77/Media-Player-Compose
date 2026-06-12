package com.example.feature.music_player.model

import com.example.core.data.util.MusicThumbnailUtil
import com.example.core.model.MusicModel
import com.example.core.model.PlayerStateModel
import com.example.core.music_media3.model.ArtworkModel

data class PlayerUiState(
    val thumbnailDominantColor: Int = MusicThumbnailUtil.DEFAULT_COLOR_PALETTE,
    val currentPlayerState: PlayerStateModel = PlayerStateModel.Initial,
    val thumbnailsList: List<ArtworkModel> = emptyList(),
    val playedMusicList: List<MusicModel> = emptyList(),
    val currentThumbnailPagerIndex: Int = 0,
    val currentPlayerPosition: Long = 0,
    val currentDeviceVolume: Int = 0,
)
