package com.example.core.domain.useCase

import com.example.core.model.PlayerStateModel
import com.example.core.music_media3.MusicServiceConnection
import kotlinx.coroutines.flow.StateFlow

class GetMusicPlayerStateUseCase(
    private val musicServiceConnection: MusicServiceConnection,
) {
    operator fun invoke(): StateFlow<PlayerStateModel> {
        return musicServiceConnection.playerState
    }
}
