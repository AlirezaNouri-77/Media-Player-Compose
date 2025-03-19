import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.dsl.LibraryExtension
import configuration.configKotlinJvm
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidConventionPlugin : Plugin<Project>{
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.android.library")
      apply(plugin = "org.jetbrains.kotlin.android")

      extensions.configure<LibraryExtension> {
        setupAndroidSdkConfig(this)
        configKotlinJvm(this)
      }

    }
  }
}