plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.ksp)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":tests"))

    implementation(project(":stone_multiplatform"))
//    ksp(project(":stone_ksp"))

    testImplementation(libs.bundles.junit)

}


