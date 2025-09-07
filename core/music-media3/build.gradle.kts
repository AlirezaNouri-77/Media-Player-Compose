plugins {
    alias(libs.plugins.mediaplayer.androidComposeConventionPlugin)
}

android {
    namespace = "com.example.core.music_media3"
}

dependencies {

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.session)

    implementation(libs.koin.android)

    implementation(project(":core:common"))
    implementation(project(":core:model"))
}
