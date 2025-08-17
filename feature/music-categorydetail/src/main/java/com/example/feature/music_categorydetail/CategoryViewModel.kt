package com.example.feature.music_categorydetail

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.util.MusicThumbnailUtilImpl
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.model.navigation.ParentCategoryRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val musicSource: MusicSourceImpl,
    private val categoryName: String,
    private val parentRoute: ParentCategoryRoute,
    private val mediaThumbnailUtil: MusicThumbnailUtilImpl,
) : ViewModel() {

    private var mCategoryUiState = MutableStateFlow(CategoryUiState())
    val uiState = mCategoryUiState
        .onStart {
            getSongs()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CategoryUiState(isLoading = true),
        )

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
                ParentCategoryRoute.FOLDER -> categoryMusicData.folder.find { it.first == categoryName }?.second
                ParentCategoryRoute.ARTIST -> categoryMusicData.artist.find { it.first == categoryName }?.second
                ParentCategoryRoute.ALBUM -> categoryMusicData.album.find { it.first == categoryName }?.second
            } ?: emptyList()
        }.collect { songs ->
            mCategoryUiState.update { it.copy(songList = songs) }
            getColorPaletteFromArtwork(songs.firstOrNull { it.artworkUri.isNotEmpty() }?.uri?.toUri())
        }
    }

    fun getColorPaletteFromArtwork(uri: Uri?) {
        viewModelScope.launch {
            val bitmap = mediaThumbnailUtil.getMusicThumbnail(uri ?: Uri.EMPTY)
            mCategoryUiState.update { it.copy(artWorkColor = mediaThumbnailUtil.getMainColorOfBitmap(bitmap)) }
        }
    }
}
