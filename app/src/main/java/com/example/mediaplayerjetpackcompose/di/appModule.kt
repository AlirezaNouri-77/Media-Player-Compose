package com.example.mediaplayerjetpackcompose.di

import com.example.feature.music_album.AlbumViewModel
import com.example.feature.music_artist.ArtistViewModel
import com.example.feature.music_categorydetail.CategoryViewModel
import com.example.feature.music_home.HomeViewModel
import com.example.feature.music_player.PlayerViewModel
import com.example.feature.music_search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

var appModule = module {

  viewModelOf(::PlayerViewModel)
  viewModelOf(::CategoryViewModel)
  viewModelOf(::SearchViewModel)

  single {
    AlbumViewModel(get(),get(named("AlbumSortDataStore")))
  }

  single {
    ArtistViewModel(get(),get(named("ArtistSortDataStore")))
  }

  single {
    HomeViewModel(get(), get(named("SongsSortDataStore")), get(named("FolderSortDataStore")), get())
  }

}
