plugins {
  alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
  namespace = "com.example.feature.music_player"
}

dependencies {

  implementation(project(":core:music-media3"))
  implementation(libs.androidx.constraintlayout.compose)

}