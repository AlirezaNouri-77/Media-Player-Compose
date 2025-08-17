plugins {
  alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
  namespace = "com.example.feature.music_categorydetail"
}

dependencies {

  implementation(project(":core:domain"))
  implementation(project(":core:common"))

}