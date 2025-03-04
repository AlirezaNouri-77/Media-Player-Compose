plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
  alias(libs.plugins.android.plugin.serialization)
}

dependencies{
  implementation(libs.jetbrains.kotlinx.serialization)
}