package com.example.mediaplayerjetpackcompose.data

import android.net.Uri
import androidx.core.net.toUri
import java.text.DecimalFormat
import java.util.Base64
import java.util.concurrent.TimeUnit

fun Int.convertMilliSecondToTime(): String {
  val hour = TimeUnit.MILLISECONDS.toHours(this.toLong())
  val minute = TimeUnit.MILLISECONDS.toMinutes(this.toLong()) % 60
  val second = TimeUnit.MILLISECONDS.toSeconds(this.toLong()) % 60
  return String.format("%02d:%02d:%02d", hour, minute, second)
}

fun String.encodeStringNavigation(): String {
  return Base64.getUrlEncoder().encodeToString(this.toByteArray())
}

fun String.decodeStringNavigation(): Uri {
  return String(Base64.getUrlDecoder().decode(this)).toUri()
}

fun Int.convertByteToReadableSize(): String {
  val result: StringBuilder = StringBuilder()
  if (this >= 1_000_000_000) {
    result.append(DecimalFormat("##.##").format(this.div(1_000_000_000f)).toString())
    result.append(" Gb")
  } else {
    result.append(DecimalFormat("##.##").format(this.div(1_000_000f)).toString())
    result.append(" Mg")
  }
  return result.toString()
}