package com.example.core.model

enum class FolderSortType() : SortType {
  NAME {
    override fun getString(): String = "Name"
  },
  SongsCount {
    override fun getString(): String = "Songs Count"
  },
}