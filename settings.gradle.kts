pluginManagement {
  includeBuild("build-logic")
  repositories {
	google()
	mavenCentral()
	gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
	google()
	mavenCentral()
  }
}

rootProject.name = "MediaPlayerJetpackCompose"

include(":app")
include(":core:common")
include(":core:designSystem")
include(":core:database")
include(":core:data")
include(":core:domain")
include(":core:model")
include(":core:music-media3")
include(":core:video-media3")
include(":core:datastore")

include(":feature:music-artist")
include(":feature:music-player")
include(":feature:music-album")
include(":feature:music-search")
include(":feature:music-home")
include(":feature:music-categorydetail")
include(":feature:video")

