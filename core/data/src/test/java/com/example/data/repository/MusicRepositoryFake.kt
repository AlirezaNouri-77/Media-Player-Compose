package com.example.data.repository

import com.example.core.domain.respository.MusicRepositoryImpl
import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MusicRepositoryFake : MusicRepositoryImpl {
  override fun getSongs(): Flow<List<MusicModel>> {
    return flow { emit(dummyList) }
  }
}

val dummyList = listOf(
  MusicModel(
    musicId = 1000002,
    uri = "content://media/external/audio/media/5005",
    path = "/storage/emulated/0/Music/UpbeatPop.mp3",
    mimeTypes = "audio/mpeg",
    name = "Upbeat Pop",
    duration = 270000L,
    size = 5500000L,
    artworkUri = "content://media/external/audio/albumart/505",
    bitrate = 256000,
    artist = "Pop Parade",
    album = "Bright Days",
    folderName = "Music"
  ),
  MusicModel(
    musicId = 1000003,
    uri = "content://media/external/audio/media/4004",
    path = "/storage/emulated/0/Music/DreamyPiano.flac",
    mimeTypes = "audio/flac",
    name = "Dreamy Piano",
    duration = 360000L,
    size = 12000000L,
    artworkUri = "content://media/external/audio/albumart/404",
    bitrate = 441000,
    artist = "Ivory Keys",
    album = "Silent Moments",
    folderName = "Music"
  ),
  MusicModel(
    musicId = 1000004,
    uri = "content://media/external/audio/media/3003",
    path = "/storage/emulated/0/Music/FunkyGroove.wav",
    mimeTypes = "audio/wav",
    name = "Funky Groove",
    duration = 210000L,
    size = 8000000L,
    artworkUri = "content://media/external/audio/albumart/303",
    bitrate = 320000,
    artist = "The Rhythm Kings",
    album = "Dancing Shoes",
    folderName = "Music 2"
  ),
  MusicModel(
    musicId = 1000005,
    uri = "content://media/external/audio/media/2002",
    path = "/storage/emulated/0/Music/CalmMelody.ogg",
    mimeTypes = "audio/ogg",
    name = "Calm Melody",
    duration = 300000L,
    size = 6000000L,
    artworkUri = "content://media/external/audio/albumart/202",
    bitrate = 128000,
    artist = "Quiet Shores",
    album = "Sunset Dreams",
    folderName = "Music"
  ),
  MusicModel(
    musicId = 10000076,
    uri = "content://media/external/audio/media/4004",
    path = "/storage/emulated/0/Music/DreamyPiano.flac",
    mimeTypes = "audio/flac",
    name = "Dreamy Piano 2",
    duration = 360000L,
    size = 12000000L,
    artworkUri = "content://media/external/audio/albumart/404",
    bitrate = 441000,
    artist = "Ivory Keys",
    album = "Sunset Dreams",
    folderName = "Music 2"
  ),
)