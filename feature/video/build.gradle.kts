plugins {
    alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
    namespace = "com.example.feature.video"
}

dependencies {

    api(project(":core:video-media3"))
    implementation(project(":core:common"))

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.session)

    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.video)
}
