plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
    alias(libs.plugins.google.protobuf)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "com.example.core.datastore"
}

protobuf {
    protoc {
        artifact = libs.google.porotbuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    api(project(":core:model"))
    api(project(":core:common"))

    api(libs.google.porotbuf.kotlinLite)

    implementation(libs.koin.android)
    implementation(libs.androidx.dataStore)

    androidTestImplementation(libs.jetbrains.kotlinx.coroutine.test)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
