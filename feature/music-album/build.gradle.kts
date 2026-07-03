plugins {
    alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
    namespace = "com.shermanrex.feature.music_album"
}

dependencies {

    implementation(project(":core:domain"))
}
