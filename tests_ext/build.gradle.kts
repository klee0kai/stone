plugins {
    java
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":stone_lib"))
    annotationProcessor(project(":stone_processor"))

    implementation(project(":tests"))

    testImplementation(libs.bundles.jupiter)
}


