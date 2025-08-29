plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

android {
    namespace = "com.example.core.domain"
}

dependencies {

    implementation(project(":core:model"))

    implementation(libs.jetbrains.kotlinx.coroutine)
    implementation(libs.androidx.media3.exoplayer)
}
