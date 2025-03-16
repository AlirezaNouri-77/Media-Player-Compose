plugins {
  alias(libs.plugins.mediaplayer.androidComposeConventionPlugin)
  alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
  namespace = "com.example.feature.video_player"
//  compileSdk = libs.versions.compileSdk.get().toInt()
//
//  defaultConfig {
//    minSdk = libs.versions.minSdk.get().toInt()
//
//    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    consumerProguardFiles("consumer-rules.pro")
//  }
//
//  buildTypes {
//    release {
//      isMinifyEnabled = false
//      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//    }
//  }
//  compileOptions {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//  }
//  kotlinOptions {
//    jvmTarget = "11"
//  }
}

dependencies {

  implementation(project(":feature:video"))
//  implementation(project(":core:designSystem"))
//  implementation(project(":core:model"))
//  implementation(project(":core:util"))

//  implementation(libs.androidx.core.ktx)
//  implementation(libs.androidx.appcompat)
//  implementation(libs.material)

//  implementation(libs.androidx.activity.compose)
//  implementation(platform(libs.androidx.compose.bom))
//  implementation(libs.androidx.ui)
//  implementation(libs.androidx.ui.graphics)
//  implementation(libs.androidx.ui.tooling.preview)
//  debugImplementation(libs.androidx.ui.tooling)
//  implementation(libs.androidx.material3)

  implementation(libs.androidx.media3.exoplayer)
  implementation(libs.androidx.media3.exoplayer.dash)
  implementation(libs.androidx.media3.ui)
  implementation(libs.androidx.media3.session)

  implementation(libs.androidx.constraintlayout.compose)

//  testImplementation(libs.junit)
//  androidTestImplementation(libs.androidx.junit)
//  androidTestImplementation(libs.androidx.espresso.core)

}