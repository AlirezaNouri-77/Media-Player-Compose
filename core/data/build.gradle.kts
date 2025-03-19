plugins {
  alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

android {
  namespace = "com.example.core.data"
}

dependencies {

  implementation(project(":core:database"))
  implementation(project(":core:common"))
  implementation(project(":core:domain"))
  implementation(project(":core:model"))

  implementation(libs.androidx.media3.exoplayer)
  
  implementation(libs.androidx.palette.ktx)

  implementation(libs.androidx.lifecycle.runtime.ktx)

  implementation(libs.koin.android)

}