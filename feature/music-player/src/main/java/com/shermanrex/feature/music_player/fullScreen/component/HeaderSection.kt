package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shermanrex.core.designsystem.R
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onTimerClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier.size(32.dp),
                onClick = onBackClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                ),
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                )
            }
            Text(
                text = "Now Playing",
                modifier = Modifier,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
            )
        }
        IconButton(
            onClick = onTimerClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White.copy(0.8f),
            ),
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.icon_timer),
                contentDescription = "",
            )
        }
    }
}

@Preview()
@Composable
private fun HeaderSectionPreview() {
    MediaPlayerJetpackComposeTheme {
        HeaderSection(onBackClick = {}, onTimerClick = {})
    }
}
