plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.ksp)
}


kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }

    linuxX64()
    mingwX64()
    wasmJs()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":stone_multiplatform"))

        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

dependencies {
    ksp(project(":stone_ksp"))

}

