package com.example.core.common

import com.example.core.common.util.convertByteToReadableSize
import com.example.core.common.util.convertMilliSecondToTime
import com.example.core.common.util.convertToReadableBitrate
import com.example.core.common.util.extractFileExtension
import com.example.core.common.util.removeFileExtension
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ExtensionsTest {
    @Test
    fun convertMilliSecondToTime() {
        val timeMap = mapOf(
            1_233_000 to "20:33",
            533_000 to "08:53",
            22_331 to "00:22",
            12_213_300 to "3:23:33",
            10_000_312 to "2:46:40",
            41_999_984 to "11:39:59",
        )

        timeMap.forEach { (key, value) ->
            val convert = key.convertMilliSecondToTime()
            assertEquals(value, convert)
        }
    }

    @Test
    fun convertToReadableBitrate() {
        val bitrate = mapOf(
            320_000 to "320Kbps",
            192_000 to "192Kbps",
            96_000 to "96Kbps",
        )

        bitrate.forEach { (key, value) ->
            val convert = key.convertToReadableBitrate()
            assertEquals(value, convert)
        }
    }

    @Test
    fun extractFileExtension() {
        val bitrate = mapOf(
            "example name 1.mp3" to "MP3",
            "example name 2.mp4" to "MP4",
            "example name 3.flac" to "FLAC",
            "example name 4.ogg" to "OGG",
        )

        bitrate.forEach { (key, value) ->
            val convert = key.extractFileExtension()
            assertEquals(value, convert)
        }
    }

    @Test
    fun removeFileExtension() {
        val bitrates = mapOf(
            "example name 1.mp3" to "example name 1",
            "example name 2.mp4" to "example name 2",
            "example name 3.flac" to "example name 3",
            "example name 4.ogg" to "example name 4",
        )

        bitrates.forEach { (key, value) ->
            val convert = key.removeFileExtension()
            assertEquals(value, convert)
        }
    }

    @Test
    fun convertByteToReadableSize() {
        val sizes = mapOf(
            23_221_232 to "23.22mg",
            1_231_442 to "1.23mg",
            123_144 to "0.12mg",
            123_144_223 to "123.14mg",
            100_000 to "0.1mg",
            1_073_741_824 to "1.0gb",
            12_356_672_543 to "11.5gb",
        )

        sizes.forEach { (key, value) ->
            val convert = key.convertByteToReadableSize()
            assertEquals(value, convert)
        }
    }
}
