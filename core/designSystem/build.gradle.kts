import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.mediaplayer.androidComposeConventionPlugin)
}

extensions.configure<LibraryExtension> {
    namespace = "com.example.core.designsystem"
}

dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation3.ui)
}
