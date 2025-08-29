package com.example.core.data.repository

import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.domain.respository.MusicRepositoryImpl
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.domain.respository.albumName
import com.example.core.domain.respository.artistName
import com.example.core.domain.respository.folderName
import com.example.core.model.MusicModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MusicSource(
    private val musicMediaStoreRepository: MusicRepositoryImpl,
    private val favoriteRepository: FavoriteRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher,
) : MusicSourceImpl {
    override fun songs(): Flow<List<MusicModel>> = combine(
        musicMediaStoreRepository.getSongs(),
        favoriteRepository.favoritesMediaIdList(),
    ) { musics, favoriteIds ->
        musics.map {
            it.copy(isFavorite = favoriteIds.contains(it.musicId.toString()))
        }
    }.flowOn(ioDispatcher)

    override fun artist(): Flow<List<Pair<artistName, List<MusicModel>>>> = songs().map { list ->
        list.groupBy { it.artist }.map { it.key to it.value }
    }.flowOn(ioDispatcher)

    override fun album(): Flow<List<Pair<albumName, List<MusicModel>>>> = songs().map { list ->
        list.groupBy { by -> by.album }.map { it.key to it.value }
    }.flowOn(ioDispatcher)

    override fun folder(): Flow<List<Pair<folderName, List<MusicModel>>>> = songs().map { list ->
        list.groupBy { by -> by.folderName }.map { it.key to it.value }
    }.flowOn(ioDispatcher)

    override fun favorite(): Flow<List<MusicModel>> = combine(
        songs(),
        favoriteRepository.favoritesMediaIdList(),
    ) { musicList, favoriteMediaIdList ->
        musicList.filterIndexed { _, item ->
            item.musicId.toString() in favoriteMediaIdList
        }
    }.flowOn(ioDispatcher)
}
