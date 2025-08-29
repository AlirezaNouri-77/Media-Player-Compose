package com.example.core.model.datastore

enum class SongsSortType : SortType {
    NAME {
        override fun getString(): String = "Name"
    },
    ARTIST {
        override fun getString(): String = "Artist"
    },
    DURATION {
        override fun getString(): String = "Duration"
    },
    SIZE {
        override fun getString(): String = "Size"
    },
}
