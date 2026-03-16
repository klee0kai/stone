plugins {
    alias(libs.plugins.kotlin.jvm)

}

tasks.test {
    useJUnitPlatform()
}


dependencies {
    implementation(project(":stone_multiplatform"))
    implementation(project(":stone_ksp"))

    testImplementation(libs.bundles.junit)
    testImplementation(libs.testing.kapt.compile)

    // not support k2
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.5.0")
}


