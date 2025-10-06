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
    exclude("com/github/klee0kai/stone/closed/")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(libs.java.inject)

    testImplementation(libs.bundles.jupiter)
}
