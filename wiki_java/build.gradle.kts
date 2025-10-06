plugins {
    application
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":tests"))
    implementation(project(":test_feature:finance:api"))

    implementation(project(":kotlin_lib"))
    annotationProcessor(project(":stone_processor"))
}

