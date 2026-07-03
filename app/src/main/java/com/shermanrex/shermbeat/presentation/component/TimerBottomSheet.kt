package com.shermanrex.shermbeat.presentation.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shermanrex.core.common.util.convertMilliSecondToTime
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.model.PlayerTimers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerBottomSheet(
    modifier: Modifier = Modifier,
    selectedTimer: PlayerTimers?,
    timerTimerLeft: Long,
    onClick: (PlayerTimers) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(true)

    ModalBottomSheet(
        modifier = modifier,
        sheetState = modalBottomSheetState,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        TimerBottomSheetContent(
            selectedTimer = selectedTimer,
            timerTimerLeft = timerTimerLeft,
            onClick = onClick,
        )
    }
}

@Composable
private fun TimerBottomSheetContent(
    selectedTimer: PlayerTimers?,
    timerTimerLeft: Long,
    onClick: (PlayerTimers) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .padding(bottom = 16.dp),
    ) {
        Text(
            text = "Timer",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(Modifier.height(8.dp))
        PlayerTimers.entries.filterNot { it == PlayerTimers.INITIAL }.onEach { timer ->
            BottomSheetItem(
                isSelected = selectedTimer == timer,
                timerData = timer,
                timerTimerLeft = timerTimerLeft,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun BottomSheetItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    timerTimerLeft: Long,
    timerData: PlayerTimers,
    onClick: (PlayerTimers) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick(timerData)
            }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = timerData.asString(timerData),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        if (isSelected && timerData != PlayerTimers.END_OFF_SONG) {
            Text(
                text = timerTimerLeft.convertMilliSecondToTime(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun TimerBottomSheetPreview() {
    MediaPlayerJetpackComposeTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TimerBottomSheetContent(
                PlayerTimers.FORTY_FIVE_MINUTE,
                timerTimerLeft = 120000L,
                onClick = {},
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun BottomSheetItemPreview() {
    MediaPlayerJetpackComposeTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            BottomSheetItem(
                isSelected = true,
                timerData = PlayerTimers.TWELVE_MINUTE,
                timerTimerLeft = 1200000L,
                onClick = {},
            )
        }
    }
}
