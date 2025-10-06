plugins {
    java
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":stone_lib"))
    implementation(project(":stone_processor"))


    testImplementation(libs.bundles.junit)
    testImplementation(libs.testing.compile)

}


