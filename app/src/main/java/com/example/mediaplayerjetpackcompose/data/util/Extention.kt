package com.example.mediaplayerjetpackcompose.data.util

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

inline fun <T : Number?> T.convertMilliSecondToTime(): String {
  if (this == null) return "00:00"
  val input = this.toLong()

  val hour = TimeUnit.MILLISECONDS.toHours(input)
  val minute = TimeUnit.MILLISECONDS.toMinutes(input) % 60
  val second = TimeUnit.MILLISECONDS.toSeconds(input) % 60
  return if (hour > 0) {
    String.format(Locale.ENGLISH, "%02d:%02d:%02d", hour, minute, second)
  } else {
    String.format(Locale.ENGLISH, "%2d:%02d", minute, second, Locale.ENGLISH)
  }
}

suspend inline fun <T> onMainDispatcher(crossinline action: suspend () -> T): T {
  return coroutineScope {
    withContext(Dispatchers.Main) {
      action()
    }
  }
}

suspend inline fun <T> onDefaultDispatcher(crossinline action: suspend () -> T): T {
  return coroutineScope {
    withContext(Dispatchers.Default) {
      action()
    }
  }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T> onIoDispatcher(limitedParallelism: Int = 0, crossinline action: suspend () -> T): T {
  return coroutineScope {
    if (limitedParallelism > 0) {
      withContext(Dispatchers.IO.limitedParallelism(limitedParallelism)) {
        action()
      }
    } else {
      withContext(Dispatchers.IO) {
        action()
      }
    }
  }
}

fun Int?.convertToKbps(): String {
  if (this==0) return ""
  return this?.div(1000)?.toString()?.plus("Kbps") ?: "None"
}

fun String.extractFileExtension(): String {
  return this.substringAfterLast(".").uppercase()
}

fun CharSequence.removeFileExtension(): String {
  val input = this.toString()
  return input.substringBeforeLast(".")
}

fun <T : Number?> T.convertByteToReadableSize(): String {
  val input = this?.toInt() ?: 0
  return if (input >= 1_000_000_000) {
    buildAnnotatedString {
      withStyle(style = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)) {
        append(DecimalFormat("##.#").format(input.div(1_000_000_000f)).toString())
      }
      withStyle(style = SpanStyle(fontSize = 13.sp, fontWeight = FontWeight.Light)) {
        append("gb")
      }
    }.toString()
  } else {
    buildAnnotatedString {
      withStyle(style = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)) {
        append(DecimalFormat("##.##").format(input.div(1_000_000f)).toString()).toString()
      }
      withStyle(style = SpanStyle(fontSize = 13.sp, fontWeight = FontWeight.Light)) {
        append("mg")
      }
    }.toString()
  }
}