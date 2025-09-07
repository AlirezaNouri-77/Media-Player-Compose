package com.example.feature.music_categorydetail

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.MusicThumbnailUtilImpl
import com.example.core.common.util.convertMilliSecondToTime
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.domain.useCase.GetMusicPlayerStateUseCase
import com.example.core.model.MediaCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val musicSource: MusicSourceImpl,
    private val categoryName: String,
    private val parentRoute: MediaCategory,
    private val musicThumbnailUtil: MusicThumbnailUtilImpl,
    private val getMusicPlayerStateUseCase: GetMusicPlayerStateUseCase,
) : ViewModel() {
    private var mCategoryUiState = MutableStateFlow(CategoryUiState())
    val uiState = mCategoryUiState
        .onStart {
            getSongs()
            observePlayerState()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CategoryUiState(isLoading = true),
        )

    private fun getColorPaletteFromArtwork(uri: Uri) {
        viewModelScope.launch {
            val bitmap = musicThumbnailUtil.getMusicThumbnail(uri)
            val color = musicThumbnailUtil.getMainColorOfBitmap(bitmap)
            mCategoryUiState.update { it.copy(thumbnailDominateColor = color) }
        }
    }

    private fun observePlayerState() = viewModelScope.launch {
        getMusicPlayerStateUseCase.invoke().collect { playerStateModel ->
            mCategoryUiState.update { it.copy(musicPlayerState = playerStateModel) }
        }
    }

    private fun getSongs() = viewModelScope.launch {
        combine(
            musicSource.album(),
            musicSource.folder(),
            musicSource.artist(),
        ) { album, folder, artist ->
            CategorizedMusicData(
                album = album,
                folder = folder,
                artist = artist,
            )
        }.map { categoryMusicData ->
            when (parentRoute) {
                MediaCategory.FOLDER -> categoryMusicData.folder.find { it.first == categoryName }?.second
                MediaCategory.ARTIST -> categoryMusicData.artist.find { it.first == categoryName }?.second
                MediaCategory.ALBUM -> categoryMusicData.album.find { it.first == categoryName }?.second
            } ?: emptyList()
        }.collect { songs ->
            val detail = "${songs.size} music, ${songs.map { it.duration }.reduce {
                acc,
                duration,
                ->
                duration + acc
            }.convertMilliSecondToTime()}"
            mCategoryUiState.update { it.copy(songList = songs, detail = detail) }
            getColorPaletteFromArtwork(songs.firstOrNull { it.artworkUri.isNotEmpty() }?.uri?.toUri() ?: Uri.EMPTY)
        }
    }
}
