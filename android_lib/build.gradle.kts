plugins {
    alias(libs.plugins.android.libarary)
    alias(libs.plugins.publish.maven)
    alias(libs.plugins.publish.stone)
}

group = "com.github.klee0kai.stone"
version = libs.versions.stone.get()

android {
    namespace = project.group.toString()
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        consumerProguardFiles("consumer-rules.pro")
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
            consumerProguardFiles("proguard-rules.pro")
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}


dependencies {
    api(project(":stone_lib"))

    implementation("androidx.appcompat:appcompat:1.6.1")
}