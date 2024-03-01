plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.example.mediaplayerjetpackcompose"
  compileSdk = 34
  
  defaultConfig {
	applicationId = "com.example.mediaplayerjetpackcompose"
	minSdk = 28
	targetSdk = 34
	versionCode = 1
	versionName = "1.0"
	
	testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	vectorDrawables {
	  useSupportLibrary = true
	}
  }
  
  buildTypes {
	release {
	  isMinifyEnabled = false
	  proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
	}
  }
  compileOptions {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
	jvmTarget = "1.8"
  }
  buildFeatures {
	compose = true
  }
  composeOptions {
	kotlinCompilerExtensionVersion = "1.5.10"
  }
  packaging {
	resources {
	  excludes += "/META-INF/{AL2.0,LGPL2.1}"
	}
  }
}

dependencies {
  
  val media3Version = "1.2.1"
  val navVersion = "2.7.7"
  val lifecycleVersion = "2.7.0"
  val constraintLayout = "1.0.1"
  val roomVersion = "2.6.1"
  val koinAndroidComposeVersion = "3.5.3"

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation(platform("androidx.compose:compose-bom:2024.02.01"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3:1.2.0")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
  
  implementation ("androidx.media3:media3-exoplayer:$media3Version")
  implementation ("androidx.media3:media3-exoplayer-dash:$media3Version")
  implementation ("androidx.media3:media3-ui:$media3Version")
  implementation("androidx.media3:media3-session:$media3Version")
  
  implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
  
  implementation("androidx.navigation:navigation-compose:$navVersion")
  
  implementation("androidx.constraintlayout:constraintlayout-compose:$constraintLayout")

  implementation ("androidx.room:room-ktx:$roomVersion")
  implementation ("androidx.room:room-runtime:$roomVersion")
  ksp("androidx.room:room-compiler:$roomVersion")

  implementation ("io.insert-koin:koin-android:$koinAndroidComposeVersion")
  implementation ("io.insert-koin:koin-androidx-compose:$koinAndroidComposeVersion")

}