plugins {
    id("org.jetbrains.kotlin.jvm").version("1.7.0")
    id("org.jetbrains.kotlin.kapt").version("1.7.21")
    alias(libs.plugins.publish.maven)
    alias(libs.plugins.publish.stone)
}

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


