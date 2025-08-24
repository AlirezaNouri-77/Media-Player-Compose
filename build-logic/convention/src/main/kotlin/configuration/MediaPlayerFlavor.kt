package configuration

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

enum class FlavorDimension {
    contentType,
}

enum class MediaPlayerFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    product(FlavorDimension.contentType)
}

fun configFlavor(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: MediaPlayerFlavor) -> Unit = {},
) {
    with(commonExtension){
        FlavorDimension.values().forEach {
            flavorDimensions += it.name
        }

        productFlavors {
            MediaPlayerFlavor.values().forEach {
                create(it.name){
                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)
                    if (this is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (it.applicationIdSuffix != null) {
                            applicationIdSuffix = it.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}