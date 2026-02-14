import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

configure<LibraryExtension> {
    namespace = "com.example.core.domain"
}

dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:music-media3"))

    implementation(libs.jetbrains.kotlinx.coroutine)
    implementation(libs.koin.core)
}
