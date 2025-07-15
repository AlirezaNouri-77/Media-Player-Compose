plugins {
  alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
  namespace = "com.example.feature.music_album"
}

dependencies {

  implementation(project(":core:domain"))

}