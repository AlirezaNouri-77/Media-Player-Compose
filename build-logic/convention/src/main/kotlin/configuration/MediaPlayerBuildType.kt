package configuration

enum class MediaPlayerBuildType(val applicationId: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}