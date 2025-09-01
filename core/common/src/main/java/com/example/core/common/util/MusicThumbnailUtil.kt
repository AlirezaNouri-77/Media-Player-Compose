package com.example.core.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.core.graphics.scale
import androidx.palette.graphics.Palette
import com.example.core.domain.respository.MusicThumbnailUtilImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class MusicThumbnailUtil(
    private var context: Context,
    private var defaultDispatcher: CoroutineDispatcher,
    private var ioDispatcher: CoroutineDispatcher,
) : MusicThumbnailUtilImpl {
    override suspend fun getMusicThumbnail(uri: Uri, width: Int, height: Int): Bitmap? {
        return withContext(defaultDispatcher) {
            runCatching {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(uri, Size(width, height), null)
                } else {
                    val mediaMetadataRetriever =
                        MediaMetadataRetriever().apply { setDataSource(context, uri) }
                    val byteArray = mediaMetadataRetriever.embeddedPicture
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
                    mediaMetadataRetriever.close()
                    bitmap.scale(width, height)
                }
            }.getOrNull()
        }
    }

    override suspend fun getMainColorOfBitmap(bitmap: Bitmap?): Int {
        return withContext(ioDispatcher) {
            if (bitmap == null) return@withContext DEFAULT_COLOR_PALETTE

            val palette = Palette.from(bitmap).generate()

            val vibrant = async { palette.getVibrantColor(DEFAULT_COLOR_PALETTE) }
            val lightVibrant = async { palette.getLightVibrantColor(DEFAULT_COLOR_PALETTE) }
            val lightMuted = async { palette.getLightMutedColor(DEFAULT_COLOR_PALETTE) }
            val darkVibrant = async { palette.getDominantColor(DEFAULT_COLOR_PALETTE) }

            return@withContext listOf(vibrant, lightVibrant, lightMuted, darkVibrant)
                .awaitAll()
                .filterNot { it == DEFAULT_COLOR_PALETTE }
                .firstOrNull { it != DEFAULT_COLOR_PALETTE } ?: DEFAULT_COLOR_PALETTE
        }
    }

    companion object {
        const val DEFAULT_COLOR_PALETTE = Color.DKGRAY
    }
}
