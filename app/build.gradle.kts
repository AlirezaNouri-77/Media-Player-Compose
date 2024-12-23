plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.android.kotlin)
  alias(libs.plugins.android.ksp)
  alias(libs.plugins.android.compose.plugin)
  alias(libs.plugins.android.plugin.serialization)
}

android {
  namespace = "com.example.mediaplayerjetpackcompose"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.example.mediaplayerjetpackcompose"
    minSdk = 28
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.ui.test.android)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  debugImplementation(libs.androidx.ui.tooling)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.test.manifest)

  implementation(libs.androidx.media3.exoplayer)
  implementation(libs.androidx.media3.exoplayer.dash)
  implementation(libs.androidx.media3.ui)
  implementation(libs.androidx.media3.session)

  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  implementation(libs.androidx.navigation.compose)

  implementation(libs.androidx.constraintlayout.compose)

  implementation(libs.jetbrains.kotlinx)

  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)

  implementation(libs.koin.android)
  implementation(libs.koin.androidx.compose)

  implementation(libs.coil.compose)
  implementation(libs.coil.video)
  implementation(kotlin("script-runtime"))

  implementation(libs.androidx.palette.ktx)
}