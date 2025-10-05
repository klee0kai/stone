plugins {
    `java-library`
    alias(libs.plugins.publish.maven)
    alias(libs.plugins.publish.stone)
}

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
    // https://mvnrepository.com/artifact/javax.inject/javax.inject
    api("javax.inject:javax.inject:1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}
