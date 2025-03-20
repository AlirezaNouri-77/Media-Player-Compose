import configuration.getLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class FeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.mediaplayer.buildLogic.androidComposeConvention")

      dependencies {

        "implementation"(project(":core:designSystem"))
        "implementation"(project(":core:data"))
        "implementation"(project(":core:domain"))
        "implementation"(project(":core:model"))
        "implementation"(project(":core:util"))

        "implementation"(getLibs.findLibrary("koin.android").get())
        "implementation"(getLibs.findLibrary("koin.android.viewmodel").get())
        "implementation"(getLibs.findLibrary("koin.androidx.compose").get())

        "implementation"(getLibs.findLibrary("androidx.lifecycle.runtime.ktx").get())
        "implementation"(getLibs.findLibrary("androidx.lifecycle.runtime.compose").get())
        "implementation"(getLibs.findLibrary("androidx.lifecycle.viewmodel.compose").get())

        "implementation"(getLibs.findLibrary("jetbrains.kotlinx.immutableCollections").get())

      }


    }
  }

}