package com.example.datastore.mapper

import com.example.core.model.SortType
import com.example.core.proto_datastore.Proto_SortType

fun SortType.toProtoSortType() : Proto_SortType {
  return when(this){
    SortType.NAME -> Proto_SortType.PROTO_SORT_TYPE_NAME
    SortType.ARTIST -> Proto_SortType.PROTO_SORT_TYPE_ARTIST
    SortType.DURATION -> Proto_SortType.PROTO_SORT_TYPE_DURATION
    SortType.SIZE -> Proto_SortType.PROTO_SORT_TYPE_SIZE
  }
}

fun Proto_SortType.toSortType() : SortType {
  return when(this){
    Proto_SortType.PROTO_SORT_TYPE_NAME -> SortType.NAME
    Proto_SortType.PROTO_SORT_TYPE_ARTIST -> SortType.ARTIST
    Proto_SortType.PROTO_SORT_TYPE_DURATION -> SortType.DURATION
    Proto_SortType.PROTO_SORT_TYPE_SIZE -> SortType.SIZE
    Proto_SortType.UNRECOGNIZED -> SortType.NAME
  }
}