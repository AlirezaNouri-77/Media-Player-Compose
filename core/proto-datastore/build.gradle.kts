plugins {
    alias(libs.plugins.mediaplayer.androidConventionPlugin)
    alias(libs.plugins.google.protobuf)
}

android {
    namespace = "com.example.core.proto_datastore"
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
    api(libs.google.porotbuf.kotlinLite)
}
