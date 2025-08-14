plugins {
  alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

android {
  namespace = "com.example.core.video_media3"
}

dependencies {

  implementation(project(":core:model"))
  implementation(project(":core:common"))

  implementation(libs.androidx.media3.exoplayer)
  implementation(libs.androidx.media3.exoplayer.dash)
  implementation(libs.androidx.media3.ui)

  implementation(libs.koin.core)
  implementation(libs.koin.android)
  implementation(libs.androidx.documentfile)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

}