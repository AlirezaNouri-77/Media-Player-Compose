package com.shermanrex.core.domain.useCase

import com.shermanrex.core.model.PlayingMusicState
import com.shermanrex.core.music_media3.MusicServiceConnection
import kotlinx.coroutines.flow.Flow

class GetMusicPlayerStateUseCase(
    private val musicServiceConnection: MusicServiceConnection,
) {
    operator fun invoke(): Flow<PlayingMusicState> {
        return musicServiceConnection.playerState
    }
}
