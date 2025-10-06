plugins {
    `java-library`
    alias(libs.plugins.publish.maven)
    alias(libs.plugins.publish.stone)
}

group = "com.github.klee0kai.stone"
version = libs.versions.stone.get()

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    isEnabled = false
}

dependencies {
    implementation(project(":stone_lib"))

    implementation("com.google.auto.service:auto-service:1.0.1")
    annotationProcessor("com.google.auto.service:auto-service:1.0.1")

    //  incap
    implementation("net.ltgt.gradle.incap:incap:0.3")
    implementation("net.ltgt.gradle.incap:incap-processor:0.3")

    // squareup - кодогенерация
    implementation("com.squareup:javapoet:1.13.0")
}


