package configuration

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

fun Project.configComposeMetric(applicationExtension: ApplicationExtension){
    applicationExtension.apply {
        tasks.withType<KotlinJvmCompile>().all {
            compilerOptions.freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${rootProject.file(".").absolutePath}/compose-metrics"
            )
            compilerOptions.freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${rootProject.file(".").absolutePath}/compose-reports"
            )
        }
    }
}

fun Project.configComposeMetric(libraryExtension: LibraryExtension){
    libraryExtension.apply {
        tasks.withType<KotlinJvmCompile>().all {
            compilerOptions.freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${rootProject.file(".").absolutePath}/compose-metrics"
            )
            compilerOptions.freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${rootProject.file(".").absolutePath}/compose-reports"
            )
        }
    }
}