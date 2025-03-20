plugins {
  alias(libs.plugins.mediaplayer.androidComposeConventionPlugin)
}

android {
  namespace = "com.example.core.music_media3"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)

  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  implementation(libs.androidx.media3.exoplayer)
  implementation(libs.androidx.media3.exoplayer.dash)
  implementation(libs.androidx.media3.ui)
  implementation(libs.androidx.media3.session)

  implementation(libs.koin.android)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  implementation(project(":core:common"))
  implementation(project(":core:data"))
  implementation(project(":core:model"))
  implementation(project(":core:util"))

}