plugins {
  alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)

}
