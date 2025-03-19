plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
  implementation(libs.koin.core)
  implementation(libs.jetbrains.kotlinx.coroutine)
}
