package configuration

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

fun Project.setupAndroidSdkConfig(
    applicationExtension: ApplicationExtension,
) {
    applicationExtension.apply {
        compileSdk = getLibs.findVersion("compileSdk").get().requiredVersion.toInt()
        defaultConfig {
            minSdk = getLibs.findVersion("minSdk").get().requiredVersion.toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        tasks.withType<KotlinJvmCompile>().all {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
                compilerOptions.freeCompilerArgs.addAll(
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xcontext-sensitive-resolution",
                )
            }
        }
        configFlavor(this)
    }
}

fun Project.setupAndroidSdkConfig(
    libraryExtension: LibraryExtension,
) {
    libraryExtension.apply {
        compileSdk = getLibs.findVersion("compileSdk").get().requiredVersion.toInt()
        defaultConfig {
            minSdk = getLibs.findVersion("minSdk").get().requiredVersion.toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        tasks.withType<KotlinJvmCompile>().all {
            compilerOptions {
                this.languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
                jvmTarget.set(JvmTarget.JVM_11)
                compilerOptions.freeCompilerArgs.addAll(
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xcontext-sensitive-resolution",
                )
            }
        }
        configFlavor(this)
    }
}