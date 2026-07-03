plugins {
    alias(libs.plugins.mediaplayer.featureModuleConventionPlugin)
}

android {
    namespace = "com.shermanrex.feature.music_categorydetail"
}

dependencies {

    implementation(project(":core:domain"))
}
