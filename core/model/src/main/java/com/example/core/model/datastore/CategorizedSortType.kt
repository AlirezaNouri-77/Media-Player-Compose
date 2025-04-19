package com.example.core.model.datastore

enum class CategorizedSortType() : SortType {
  NAME {
    override fun getString(): String = "Name"
  },
  SongsCount {
    override fun getString(): String = "Songs Count"
  },
}