package com.example.core.domain.useCase

import com.example.core.model.MusicPlayerState
import com.example.core.music_media3.MusicServiceConnection
import kotlinx.coroutines.flow.Flow

class GetMusicPlayerStateUseCase(
    private val musicServiceConnection: MusicServiceConnection,
) {
    operator fun invoke(): Flow<MusicPlayerState> {
        return musicServiceConnection.playerState
    }
}
