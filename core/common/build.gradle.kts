plugins {
  alias(libs.plugins.mediaplayer.androidComposeConventionPlugin)
}

android {
  namespace = "com.example.core.common"
}

dependencies {

  implementation(project(":core:domain"))

  implementation(libs.koin.core)
  implementation(libs.koin.android)
  implementation(libs.jetbrains.kotlinx.coroutine)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)

  implementation(libs.androidx.palette.ktx)

  testImplementation(libs.junit)
}
