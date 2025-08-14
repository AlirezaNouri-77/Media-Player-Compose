plugins {
  alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

android {
  defaultConfig {
    consumerProguardFiles("consumer-proguard-rules.pro")
  }
  namespace = "com.example.core.datastore"
}

dependencies {

  api(project(":core:proto-datastore"))
  api(project(":core:model"))
  api(project(":core:common"))

  api(libs.androidx.dataStore)

  implementation(libs.koin.android)

  androidTestImplementation(libs.jetbrains.kotlinx.coroutine.test)

  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}