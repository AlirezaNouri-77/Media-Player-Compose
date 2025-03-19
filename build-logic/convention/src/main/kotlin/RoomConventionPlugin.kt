import configuration.getLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.google.devtools.ksp")

      dependencies {

        "implementation"(getLibs.findLibrary("room.ktx").get())
        "implementation"(getLibs.findLibrary("room.runtime").get())
        "ksp"(getLibs.findLibrary("room.compiler").get())

      }

    }
  }
}