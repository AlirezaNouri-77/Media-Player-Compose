// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.kotlin) apply false
  alias(libs.plugins.android.ksp) apply false
  alias(libs.plugins.android.compose.plugin) apply false
  alias(libs.plugins.android.plugin.serialization) apply false
}