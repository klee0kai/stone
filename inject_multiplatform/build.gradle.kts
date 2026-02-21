plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publish.stone)
    alias(libs.plugins.publish.maven)
}

group = "com.github.klee0kai.stone"
version = libs.versions.stone.get()

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
        commonMain.dependencies {
            api(libs.kotlinx.coroutines)
        }
        jvmMain.dependencies {
            api(libs.java.inject)
        }

    }
}

val isMac = System.getProperty("os.name").contains("Mac")
if (isMac) kotlin {
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }


        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("Stone")
            description.set("Library DI designed on weak references.")
        }
    }
}

