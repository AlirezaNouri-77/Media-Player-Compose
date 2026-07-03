package com.shermanrex.feature.music_player

import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.model.PlayerTimers

sealed interface PlayerActions {
    data object PausePlayer : PlayerActions

    data object ResumePlayer : PlayerActions

    data object MoveNextPlayer : PlayerActions

    data object OnShuffleMode : PlayerActions

    data object OnShowTimerBottomSheet : PlayerActions

    data object OnHideTimerBottomSheet : PlayerActions

    data class PlaySongs(val index: Int, val list: List<MusicModel>) : PlayerActions

    data class OnTimerClick(val timers: PlayerTimers) : PlayerActions

    data class OnFavoriteToggle(val mediaId: String) : PlayerActions

    data class OnMoveToIndex(val value: Int, val musicId: String) : PlayerActions

    data class MovePreviousPlayer(val seekToStart: Boolean) : PlayerActions

    data class SeekTo(val value: Long) : PlayerActions

    data class OnRepeatMode(val value: PlayerRepeatMode) : PlayerActions

    data class UpdateArtworkPageIndex(val value: Int) : PlayerActions
}
