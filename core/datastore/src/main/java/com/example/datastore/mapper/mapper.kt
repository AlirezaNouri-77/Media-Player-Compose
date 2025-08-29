package com.example.datastore.mapper

import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.datastore.SongsSortType
import com.example.core.proto_datastore.Proto_DataStore_Folder
import com.example.core.proto_datastore.Proto_SortType

fun SongsSortType.toProtoSortType(): Proto_SortType {
    return when (this) {
        SongsSortType.NAME -> Proto_SortType.PROTO_SORT_TYPE_NAME
        SongsSortType.ARTIST -> Proto_SortType.PROTO_SORT_TYPE_ARTIST
        SongsSortType.DURATION -> Proto_SortType.PROTO_SORT_TYPE_DURATION
        SongsSortType.SIZE -> Proto_SortType.PROTO_SORT_TYPE_SIZE
    }
}

fun Proto_SortType.toSortType(): SongsSortType {
    return when (this) {
        Proto_SortType.PROTO_SORT_TYPE_NAME -> SongsSortType.NAME
        Proto_SortType.PROTO_SORT_TYPE_ARTIST -> SongsSortType.ARTIST
        Proto_SortType.PROTO_SORT_TYPE_DURATION -> SongsSortType.DURATION
        Proto_SortType.PROTO_SORT_TYPE_SIZE -> SongsSortType.SIZE
        Proto_SortType.UNRECOGNIZED -> SongsSortType.NAME
    }
}

fun CategorizedSortType.toProtoSortType(): Proto_DataStore_Folder {
    return when (this) {
        CategorizedSortType.NAME -> Proto_DataStore_Folder.Name
        CategorizedSortType.SongsCount -> Proto_DataStore_Folder.Songs_Count
    }
}

fun Proto_DataStore_Folder.toFolderSortType(): CategorizedSortType {
    return when (this) {
        Proto_DataStore_Folder.Name -> CategorizedSortType.NAME
        Proto_DataStore_Folder.Songs_Count -> CategorizedSortType.SongsCount
        Proto_DataStore_Folder.UNRECOGNIZED -> CategorizedSortType.NAME
    }
}
