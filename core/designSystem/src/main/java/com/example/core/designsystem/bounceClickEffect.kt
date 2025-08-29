package com.example.core.designsystem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.bounceClickEffect(): Modifier {
    var isPressed by rememberSaveable { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.85f else 1f)

    return this
        .pointerInput(Unit) { // The 'key1 = Unit' means this doesn't restart often
            awaitEachGesture {
                while (true) {
                    val event = awaitPointerEvent()
                    // consume all changes
                    isPressed = event.type == PointerEventType.Press
                    event.changes.forEach { it.consume() }
                }
            }
        }
        .graphicsLayer {
            this.scaleX = this.scaleX * scale
            this.scaleY = this.scaleY * scale
        }
}
