package com.example.feature.music_player.miniPlayer.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.NoRippleEffect
import com.example.core.designsystem.R
import com.example.core.designsystem.bounceClickEffect

@Composable
fun MiniPlayerActions(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClickPlayAndPause: () -> Unit,
) {
    IconButton(
        modifier = modifier.bounceClickEffect()
            .padding(6.dp)
            .size(18.dp),
        onClick = onClickPlayAndPause,
        interactionSource = NoRippleEffect,
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play),
            contentDescription = "",
            tint = Color.White,
        )
    }
}
