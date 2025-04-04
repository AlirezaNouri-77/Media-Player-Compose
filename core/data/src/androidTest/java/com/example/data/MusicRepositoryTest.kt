package com.example.data

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.example.core.data.repository.MusicRepositoryImpl
import com.example.core.model.MusicModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.Before

class MusicRepositoryTest : MusicRepositoryImpl {

  @MockK
  lateinit var contentResolver: ContentResolver
  @MockK
  lateinit var cursor: Cursor

  @Before
  fun setup() {
    contentResolver = mockk()
  }

  override fun getSongs(): Flow<List<MusicModel>> {

    every { contentResolver.query(any<Uri>(), any(), any(), any(), any()) } returns cursor
    every { cursor } returns dummyMusicList

  }

}

var dummyMusicList = listOf(
  MusicModel(
    musicId = 5005L,
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
    musicId = 1001L,
    uri = "content://media/external/audio/media/1001",
    path = "/storage/emulated/0/Music/EnergeticBeat.mp3",
    mimeTypes = "audio/mpeg",
    name = "Energetic Beat",
    duration = 180000L,
    size = 4500000L,
    artworkUri = "content://media/external/audio/albumart/101",
    bitrate = 160000,
    artist = "SynthWave Stars",
    album = "Neon Nights",
    folderName = "Music"
  ),
  MusicModel(
    musicId = 2002L,
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
    musicId = 3003L,
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
    folderName = "Music"
  )
)