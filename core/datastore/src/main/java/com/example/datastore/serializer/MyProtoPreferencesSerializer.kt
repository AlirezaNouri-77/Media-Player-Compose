package com.example.datastore.serializer

import androidx.datastore.core.Serializer
import com.example.core.datastore.MyProtoPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

class MyProtoPreferencesSerializer : Serializer<MyProtoPreferences> {
    override val defaultValue: MyProtoPreferences = MyProtoPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): MyProtoPreferences {
        try {
            return MyProtoPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw Exception("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: MyProtoPreferences, output: OutputStream) = t.writeTo(output)
}
