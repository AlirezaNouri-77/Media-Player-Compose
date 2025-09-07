plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

android {
    namespace = "com.example.core.domain"
}

dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:music-media3"))

    implementation(libs.jetbrains.kotlinx.coroutine)
    implementation(libs.koin.core)
}
