plugins {
  alias(libs.plugins.mediaplayer.androidComposeConventionPlugin)
}

android {
  namespace = "com.example.core.designsystem"
}

dependencies {

  implementation(project(":core:model"))
  implementation(project(":core:util"))

  implementation(libs.coil.compose)

}