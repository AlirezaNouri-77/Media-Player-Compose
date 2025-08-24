import com.android.build.gradle.LibraryExtension
import configuration.getLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.android.library")
      apply(plugin = "org.jetbrains.kotlin.android")
      apply(plugin = "org.jetbrains.kotlin.plugin.compose")

      extensions.configure<LibraryExtension> {
        setupAndroidSdkConfig(this)
        buildFeatures {
          compose = true
        }
      }

      this.dependencies {
        val bom = getLibs.findLibrary("androidx.compose.bom").get()
        "implementation"(getLibs.findLibrary("androidx.activity.compose").get())
        "implementation"(getLibs.findLibrary("androidx.compose.foundation").get())
        "implementation"(platform(bom))
        "androidTestImplementation"(platform(bom))
        "implementation"(getLibs.findLibrary("androidx.ui.tooling.preview").get())
        "implementation"(getLibs.findLibrary("androidx.material3").get())
        "implementation"(getLibs.findLibrary("androidx.ui.graphics").get())
        "debugImplementation"(getLibs.findLibrary("androidx.ui.tooling").get())
      }

    }
  }
}

