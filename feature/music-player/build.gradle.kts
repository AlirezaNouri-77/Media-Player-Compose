plugins {
    alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
    namespace = "com.shermanrex.feature.music_player"
}

dependencies {

    api(project(":core:music-media3"))
    implementation(project(":core:common"))

    implementation(libs.androidx.constraintlayout.compose)
}
