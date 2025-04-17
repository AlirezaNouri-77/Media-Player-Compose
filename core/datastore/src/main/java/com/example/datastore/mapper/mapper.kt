package com.example.datastore.mapper

import com.example.core.model.FolderSortType
import com.example.core.model.SongsSortType
import com.example.core.proto_datastore.Proto_DataStore_Folder
import com.example.core.proto_datastore.Proto_SortType

fun SongsSortType.toProtoSortType() : Proto_SortType {
  return when(this){
    SongsSortType.NAME -> Proto_SortType.PROTO_SORT_TYPE_NAME
    SongsSortType.ARTIST -> Proto_SortType.PROTO_SORT_TYPE_ARTIST
    SongsSortType.DURATION -> Proto_SortType.PROTO_SORT_TYPE_DURATION
    SongsSortType.SIZE -> Proto_SortType.PROTO_SORT_TYPE_SIZE
  }
}

fun Proto_SortType.toSortType() : SongsSortType {
  return when(this){
    Proto_SortType.PROTO_SORT_TYPE_NAME -> SongsSortType.NAME
    Proto_SortType.PROTO_SORT_TYPE_ARTIST -> SongsSortType.ARTIST
    Proto_SortType.PROTO_SORT_TYPE_DURATION -> SongsSortType.DURATION
    Proto_SortType.PROTO_SORT_TYPE_SIZE -> SongsSortType.SIZE
    Proto_SortType.UNRECOGNIZED -> SongsSortType.NAME
  }
}

fun FolderSortType.toProtoSortType() : Proto_DataStore_Folder {
  return when(this){
    FolderSortType.NAME -> Proto_DataStore_Folder.Name
    FolderSortType.SongsCount -> Proto_DataStore_Folder.Songs_Count
  }
}

fun Proto_DataStore_Folder.toFolderSortType() : FolderSortType {
  return when(this){
    Proto_DataStore_Folder.Name -> FolderSortType.NAME
    Proto_DataStore_Folder.Songs_Count -> FolderSortType.SongsCount
    Proto_DataStore_Folder.UNRECOGNIZED -> FolderSortType.NAME
  }
}