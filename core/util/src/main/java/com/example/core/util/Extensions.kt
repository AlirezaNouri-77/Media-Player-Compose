package com.example.core.util

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun <T : Number?> T.convertMilliSecondToTime(): String {
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

fun Int?.convertToKbps(): String {
  if (this == 0) return "None"
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
  if (this?.toLong() == 0L || this == null) return "None"

  val input = this.toLong()
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