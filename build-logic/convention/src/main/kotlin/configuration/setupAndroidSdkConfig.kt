import com.android.build.api.dsl.CommonExtension
import configuration.getLibs
import org.gradle.api.Project

fun Project.setupAndroidSdkConfig(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {
    compileSdk = getLibs.findVersion("compileSdk").get().requiredVersion.toInt()
    defaultConfig {
      minSdk = getLibs.findVersion("minSdk").get().requiredVersion.toInt()
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }
}