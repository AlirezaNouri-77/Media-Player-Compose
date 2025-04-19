package com.example.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.core.proto_datastore.SortPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

class SortPreferencesSerializer : Serializer<SortPreferences> {

  override val defaultValue: SortPreferences = SortPreferences.getDefaultInstance()

  override suspend fun readFrom(input: InputStream): SortPreferences {
    try {
      return SortPreferences.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
      throw CorruptionException("Cannot read proto.", exception)
    }
  }

  override suspend fun writeTo(t: SortPreferences, output: OutputStream) = t.writeTo(output)

}