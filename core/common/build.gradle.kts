plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
  implementation(libs.koin.core)
  implementation(libs.jetbrains.kotlinx.coroutine)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)

  testImplementation(libs.junit)
}
