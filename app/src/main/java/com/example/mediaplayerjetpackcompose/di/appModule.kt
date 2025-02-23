package com.example.mediaplayerjetpackcompose.di

import androidx.media3.exoplayer.ExoPlayer
import com.example.mediaplayerjetpackcompose.core.data.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.presentation.screen.music.ShareViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.album.AlbumViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.artist.ArtistViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.category.CategoryViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.home.HomeViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.search.SearchViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

var appModule = module {

//  single {
//    Room.databaseBuilder(
//      context = androidApplication().applicationContext,
//      AppDataBase::class.java, "DataBase_MediaPlayer",
//    ).build()
//  }

  factory { ExoPlayer.Builder(androidApplication().applicationContext).build() }
//  single { get<AppDataBase>().dataBaseDao() }

//  factory(named("CoroutineMain")) { CoroutineScope(Dispatchers.Main.immediate) }

//  single {
//    MusicMediaStoreRepository(
//      androidApplication().applicationContext.contentResolver,
//    )
//  }

//  single { MediaThumbnailUtil(androidApplication().applicationContext,get(named("Default")),get(named("IO"))) }
//  single { DeviceVolumeManager(androidApplication().applicationContext) }
//  single { VideoMediaMetaData(get(),get(named("IO"))) }
//  single { SearchMusicManager(get()) }

//  single { FavoriteMusicManager(get(),get(named("IO"))) }

 // single { MusicSourceRepository(get(),get()) }

//  single<MediaStoreRepositoryImpl<VideoModel>> { VideoMediaStoreRepository(androidApplication().applicationContext.contentResolver) }
 // single { MusicServiceConnection(androidApplication().applicationContext, get(named("CoroutineMain"))) }

  viewModelOf(::ShareViewModel)
  viewModelOf(::VideoPageViewModel)
  viewModelOf(::AlbumViewModel)
  viewModelOf(::CategoryViewModel)
  viewModelOf(::ArtistViewModel)
  viewModelOf(::SearchViewModel)
  viewModelOf(::HomeViewModel)

}
