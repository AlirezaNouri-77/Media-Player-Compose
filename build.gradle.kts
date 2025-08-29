import com.android.build.api.dsl.ApplicationExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.kotlin) apply false
  alias(libs.plugins.android.ksp) apply false
  alias(libs.plugins.android.compose.plugin) apply false
  alias(libs.plugins.android.plugin.serialization) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.jetbrains.kotlin.jvm) apply false
  alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
      debug.set(true)
      verbose.set(true)
      android.set(false)
      outputToConsole.set(true)
      outputColorName.set("RED")
      ignoreFailures.set(true)
      enableExperimentalRules.set(true)
      baseline.set(file("my-project-ktlint-baseline.xml"))
      reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
      }
      filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
      }
    }
  this.beforeEvaluate {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
      dependsOn("ktlintFormat")
    }
  }
}