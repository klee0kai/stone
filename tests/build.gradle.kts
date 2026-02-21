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
    wasmJs {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":stone_multiplatform"))
            }
        }

        val jsMain by getting
        val wasmJsMain by getting


        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

    }
}

dependencies {
    ksp(project(":stone_ksp"))

}

