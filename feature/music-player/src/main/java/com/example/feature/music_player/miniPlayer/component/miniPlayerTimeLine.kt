package com.example.feature.music_player.miniPlayer.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal fun Modifier.miniPlayerTimeLine(
    currentMusicPosition: Long,
    currentPlayerDuration: Int
): Modifier {
    return this.drawWithContent {
        drawContent()

        val margin = 15.dp.toPx()
        // margin times by 2 because i applied 15.dp to end and start
        val progress =
            (currentMusicPosition * (this.size.width - margin.times(2))) / currentPlayerDuration

        drawLine(
            color = Color.White,
            alpha = 0.4f,
            strokeWidth = 2.dp.toPx(),
            start = Offset(x = margin, y = this.size.height + 6.dp.toPx()),
            end = Offset(
                x = this.size.width - margin,
                y = this.size.height + 6.dp.toPx()
            )
        )
        drawLine(
            color = Color.White,
            strokeWidth = 2.dp.toPx(),
            start = Offset(x = 15.dp.toPx(), y = this.size.height + 6.dp.toPx()),
            end = Offset(x = progress + margin, y = this.size.height + 6.dp.toPx())
        )

    }
}