import com.android.build.api.dsl.LibraryExtension
import configuration.configComposeMetric
import configuration.setupAndroidSdkConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidConventionPlugin : Plugin<Project>{
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.android.library")

      extensions.configure<LibraryExtension> {
        setupAndroidSdkConfig(libraryExtension = this)
        configComposeMetric(this)
      }

    }
  }
}