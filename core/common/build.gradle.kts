plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
  alias(libs.plugins.android.ksp)
}

dependencies {
  implementation(libs.koin.core)
  implementation(libs.jetbrains.kotlinx.coroutine)
}
