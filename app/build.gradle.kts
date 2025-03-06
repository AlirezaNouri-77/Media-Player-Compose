plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.android.kotlin)
  alias(libs.plugins.android.ksp)
  alias(libs.plugins.android.plugin.serialization)
  alias(libs.plugins.android.compose.plugin)
}

android {
  namespace = "com.example.mediaplayerjetpackcompose"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.example.mediaplayerjetpackcompose"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
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
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
      kotlinOptions.freeCompilerArgs += listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
             project.buildDir.absolutePath + "/compose_metrics"
      )
      kotlinOptions.freeCompilerArgs += listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
             project.buildDir.absolutePath + "/compose_metrics"
      )
    }
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

  debugImplementation(libs.squareup.leakcanary)

  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  implementation(libs.androidx.navigation.compose)

  implementation(libs.androidx.constraintlayout.compose)

  implementation(libs.jetbrains.kotlinx.immutableCollections)
  implementation(libs.jetbrains.kotlinx.serialization)

  implementation(libs.koin.android)
  implementation(libs.koin.androidx.compose)
  implementation(libs.koin.android.viewmodel)

  implementation(libs.coil.compose)
  implementation(libs.coil.video)
  implementation(kotlin("script-runtime"))

  implementation(project(":core:common"))
  implementation(project(":core:designSystem"))
  implementation(project(":core:music-media3"))
  implementation(project(":core:database"))
  implementation(project(":core:data"))
  implementation(project(":core:model"))
  implementation(project(":core:domain"))
  implementation(project(":core:util"))

  implementation(project(":feature:music-artist"))
  implementation(project(":feature:music-home"))
  implementation(project(":feature:music-album"))
  implementation(project(":feature:music-player"))
  implementation(project(":feature:music-search"))
  implementation(project(":feature:music-categorydetail"))
  implementation(project(":feature:video"))
  implementation(project(":feature:video-player"))

}