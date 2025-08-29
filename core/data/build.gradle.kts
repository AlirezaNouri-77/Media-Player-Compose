plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
}

android {
    namespace = "com.example.core.data"
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
