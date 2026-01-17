pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

rootProject.name = "Stone"
includeBuild("plugin_publish")
include("weakref_multiplatform")
include("inject_multiplatform")
include(":stone_lib")
include(":stone_multiplatform")
include(":stone_processor")
include(":android_lib")
include(":kotlin_lib")

include(":wiki_kotlin")
include(":tests")
include(":tests_ext")
include(":tests_kotlin")
include(":tests_wraps_kotlin")
include(":tests_compile")
include(":test_feature:companies:consulting")
include(":test_feature:hr:api")
include(":test_feature:hr:impl")
include(":test_feature:planning:api")
include(":test_feature:planning:impl")
include(":test_feature:finance:api")
include(":test_feature:finance:impl")
include(":test_feature_core_deps")

include("stone_ksp")