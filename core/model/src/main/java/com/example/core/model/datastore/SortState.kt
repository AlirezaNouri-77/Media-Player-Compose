package com.example.core.model.datastore

interface SortState<T : SortType> {
  val sortType: T
  val isDec: Boolean
}