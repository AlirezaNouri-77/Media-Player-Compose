package com.shermanrex.shermbeat.di

import com.shermanrex.feature.music_album.AlbumViewModel
import com.shermanrex.feature.music_artist.ArtistViewModel
import com.shermanrex.feature.music_categorydetail.CategoryViewModel
import com.shermanrex.feature.music_home.HomeViewModel
import com.shermanrex.feature.music_player.PlayerViewModel
import com.shermanrex.feature.music_search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

var appModule = module {

    viewModelOf(::PlayerViewModel)
    viewModelOf(::CategoryViewModel)
    viewModelOf(::SearchViewModel)

    viewModel {
        AlbumViewModel(get(), get(named("AlbumSortDataStore")), get())
    }

    viewModel {
        ArtistViewModel(get(), get(named("ArtistSortDataStore")), get())
    }

    viewModel {
        HomeViewModel(
            get(),
            get(named("SongsSortDataStore")),
            get(named("FolderSortDataStore")),
            get(),
            get(),
        )
    }
}
