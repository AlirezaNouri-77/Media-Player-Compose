package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shermanrex.core.designsystem.R
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme

@Composable
fun UtilityActionComponent(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onTimerIconClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onTimerIconClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White.copy(0.8f),
            ),
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.icon_timer),
                contentDescription = "",
            )
        }
        IconButton(
            onClick = onFavoriteClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White.copy(0.8f),
            ),
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
private fun UtilityActionComponentPreview() {
    MediaPlayerJetpackComposeTheme {
        UtilityActionComponent(
            isFavorite = true,
            onTimerIconClick = {},
            onFavoriteClick = {},
        )
    }
}
