package com.example.video_media3.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.core.graphics.scale
import com.example.video_media3.model.VideoThumbnailUtilImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class VideoThumbnailUtil(
    private var defaultDispatcher: CoroutineDispatcher,
    private var context: Context,
) : VideoThumbnailUtilImpl {
    override suspend fun getVideoThumbNail(uri: Uri, position: Long): Bitmap? {
        return withContext(defaultDispatcher) {
            runCatching {
                val mediaMetadataRetriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mediaMetadataRetriever.use {
                        it.getScaledFrameAtTime(
                            position * 1000,
                            OPTION_CLOSEST_SYNC,
                            SCALE_WIDTH,
                            SCALE_WIDTH,
                        )
                    }
                } else {
                    val imageBitmap = mediaMetadataRetriever.getScaledFrameAtTime(
                        position * 1000,
                        OPTION_CLOSEST_SYNC,
                        SCALE_WIDTH,
                        SCALE_HEIGHT,
                    )
                    mediaMetadataRetriever.close()
                    imageBitmap
                }
            }.getOrNull()
        }
    }

    override suspend fun getVideoThumbNail(uri: Uri): Bitmap? {
        return withContext(defaultDispatcher) {
            runCatching {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(
                        uri,
                        Size(SCALE_WIDTH, SCALE_HEIGHT),
                        null,
                    )
                } else {
                    val mediaMetadataRetriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
                    val byteArray = mediaMetadataRetriever.embeddedPicture
                    val bitmap = BitmapFactory.decodeByteArray(
                        byteArray,
                        0,
                        byteArray!!.size,
                    )
                    mediaMetadataRetriever.close()
                    bitmap.scale(SCALE_WIDTH, SCALE_HEIGHT)
                }
            }.getOrNull()
        }
    }

    companion object {
        const val SCALE_WIDTH = 200
        const val SCALE_HEIGHT = 200
    }
}
