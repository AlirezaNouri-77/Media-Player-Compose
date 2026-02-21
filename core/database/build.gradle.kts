import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
    alias(libs.plugins.mediaplayer.roomConventionPlugin)
}

extensions.configure<LibraryExtension> {
    namespace = "com.example.core.database"
}

dependencies {

    implementation(libs.koin.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.jetbrains.kotlinx.coroutine.test)
}
