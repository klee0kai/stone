plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.publish.maven)
    alias(libs.plugins.publish.stone)
}

group = "com.github.klee0kai.stone"
version = libs.versions.stone.get()

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(project(":stone_lib"))
}


