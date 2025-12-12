package com.example.core.designsystem

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CategoryListItem(
    categoryName: String,
    musicListSize: Int,
    thumbnailUri: String?,
    onClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
) {
    with(sharedTransitionScope) {
        Surface(
            onClick = { onClick.invoke(categoryName) },
            color = Color.Transparent,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                MusicThumbnail(
                    uri = thumbnailUri?.toUri(),
                    modifier = Modifier
                        .size(size = 60.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("thumbnailKey$categoryName"),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        ),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState("categoryKey$categoryName"),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        ),
                        text = categoryName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                    )
                    Text(
                        text = "$musicListSize Music",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryListItemPreview() {
    MediaPlayerJetpackComposeTheme {
        CategoryListItem(
            categoryName = "Song name name name name name name name",
            musicListSize = 2,
            sharedTransitionScope = LocalSharedTransitionScope.current,
            thumbnailUri = "",
            onClick = {},
        )
    }
}
