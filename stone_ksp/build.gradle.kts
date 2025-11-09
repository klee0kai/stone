plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = "com.github.klee0kai.thekey.stone.ksp"
version = libs.versions.stone.get()


kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":stone_multiplatform"))
            implementation(libs.bundles.kotlin)
            implementation(libs.bundles.kotlinpoet)
            implementation(libs.ksp)
        }
    }
}



