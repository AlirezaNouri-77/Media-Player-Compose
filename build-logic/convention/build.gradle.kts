import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_11
  }
}

dependencies {
  compileOnly(libs.androidGradlePlugin)
  compileOnly(libs.kotlinGradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidCompose") {
      id = libs.plugins.mediaplayer.androidComposeConventionPlugin.get().pluginId
      implementationClass = "AndroidComposeConventionPlugin"
    }
    register("android") {
      id = libs.plugins.mediaplayer.androidConventionPlugin.get().pluginId
      implementationClass = "AndroidConventionPlugin"
    }
    register("featureModule") {
      id = libs.plugins.mediaplayer.featureModuleConventionPlugin.get().pluginId
      implementationClass = "FeatureConventionPlugin"
    }
    register("RoomConvention") {
      id = libs.plugins.mediaplayer.roomConventionPlugin.get().pluginId
      implementationClass = "RoomConventionPlugin"
    }
    register("composeMetricConvention") {
      id = libs.plugins.mediaplayer.composeMetricConventionPlugin.get().pluginId
      implementationClass = "ComposeMetricConventionPlugin"
    }
  }
}