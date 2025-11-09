plugins {
    `java-library`
//    alias(libs.plugins.publish.maven)
//    alias(libs.plugins.publish.stone)
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

    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service)

    implementation(libs.incap)
    implementation(libs.incap.processor)

    implementation(libs.javapoet)
}


