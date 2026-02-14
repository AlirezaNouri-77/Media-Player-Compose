import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import configuration.MediaPlayerBuildType
import configuration.configComposeMetric
import configuration.setupAndroidSdkConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplication: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            extensions.configure<ApplicationExtension>{
                setupAndroidSdkConfig(this)
                configComposeMetric(this)
                buildFeatures {
                    compose = true
                }
                buildTypes {
                    debug {
                        applicationIdSuffix = MediaPlayerBuildType.DEBUG.applicationId
                    }
                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        applicationIdSuffix = MediaPlayerBuildType.RELEASE.applicationId
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                        signingConfig = signingConfigs.getByName("debug")
                    }
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }
        }
    }
}