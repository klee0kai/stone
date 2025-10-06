plugins {
    java
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":stone_lib"))
    annotationProcessor(project(":stone_processor"))

    testImplementation(libs.bundles.jupiter)
}

