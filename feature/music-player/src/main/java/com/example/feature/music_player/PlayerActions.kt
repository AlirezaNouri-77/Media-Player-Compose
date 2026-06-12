package com.example.feature.music_player

import com.example.core.model.MusicModel

sealed interface PlayerActions {
    data object PausePlayer : PlayerActions

    data object ResumePlayer : PlayerActions

    data object MoveNextPlayer : PlayerActions

    data object onShuffleMode : PlayerActions

    data class PlaySongs(val index: Int, val list: List<MusicModel>) : PlayerActions

    data class OnFavoriteToggle(val mediaId: String) : PlayerActions

    data class OnMoveToIndex(val value: Int, val musicId: String) : PlayerActions

    data class MovePreviousPlayer(val seekToStart: Boolean) : PlayerActions

    data class SeekTo(val value: Long) : PlayerActions

    data class OnRepeatMode(val value: Int) : PlayerActions

    data class UpdateArtworkPageIndex(val value: Int) : PlayerActions
}
