plugins {
    java
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":stone_lib"))
    implementation(project(":stone_processor"))


    testImplementation(libs.bundles.jupiter)
    testImplementation(libs.testing.compile)

}


