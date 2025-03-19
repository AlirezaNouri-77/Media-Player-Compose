plugins {
  alias(libs.plugins.mediaplayer.androidConventionPlugin)
  alias(libs.plugins.mediaplayer.roomConventionPlugin)
}

android {
  namespace = "com.example.core.database"
}

dependencies {

  implementation(libs.koin.android)

}