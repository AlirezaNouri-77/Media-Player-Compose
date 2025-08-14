plugins {
  alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
  namespace = "com.example.feature.video_player"
}

dependencies {

  api(project(":feature:video"))
  implementation(project(":core:common"))

  implementation(libs.androidx.media3.exoplayer)
  implementation(libs.androidx.media3.exoplayer.dash)
  implementation(libs.androidx.media3.ui)
  implementation(libs.androidx.media3.session)

  implementation(libs.androidx.constraintlayout.compose)

}