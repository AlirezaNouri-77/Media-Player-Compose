package com.example.feature.video.item

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.example.core.common.util.convertByteToReadableSize
import com.example.core.common.util.convertMilliSecondToTime
import com.example.core.common.util.extractFileExtension
import com.example.core.common.util.removeFileExtension
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.VideoModel
import com.example.feature.video.R

@Composable
fun VideoMediaItem(
    item: VideoModel,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onItemClick: () -> Unit,
) {
    Surface(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
        ) {
            Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(item.uri).videoFrameMillis(15_000L).build(),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                      .align(Alignment.Center)
                      .size(120.dp, 80.dp)
                      .clip(RoundedCornerShape(8.dp)),
                    loading = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f)),
                        ) {
                            Image(
                                painter = painterResource(R.drawable.icon_video),
                                colorFilter = ColorFilter.tint(Color.White),
                                contentDescription = ""
                            )
                        }
                    },
                )
                Text(
                    text = item.duration.convertMilliSecondToTime(),
                    modifier = Modifier
                      .align(Alignment.BottomEnd)
                      .padding(end = 4.dp, bottom = 4.dp)
                      .drawWithCache {
                        onDrawBehind {
                          drawRoundRect(
                            color = Color.Black,
                            topLeft = Offset(x = -10f, y = 0f),
                            size = Size(width = this.size.width + 10f, height = this.size.height),
                            alpha = 0.4f,
                            cornerRadius = CornerRadius(x = 12f, y = 12f),
                          )
                        }
                      },
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item.name.removeFileExtension(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = contentColor,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "${item.height}x${item.width}, ${item.size.convertByteToReadableSize()}, ${item.name.extractFileExtension()}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    color = contentColor.copy(alpha = 0.6f),
                )
            }
        }
    }

}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    MediaPlayerJetpackComposeTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
            VideoMediaItem(
                item = VideoModel.Dummy,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onItemClick = {},
            )
        }
    }
}