import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

extensions.configure<LibraryExtension> {
    namespace = "com.shermanrex.core.data"
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))

    implementation(libs.androidx.media3.exoplayer)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.palette.ktx)

    implementation(libs.koin.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)

    testImplementation(libs.jetbrains.kotlinx.coroutine.test)
    testImplementation(libs.androidx.test.core)
}
