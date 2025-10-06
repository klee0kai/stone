plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":tests"))
    implementation(project(":test_feature:finance:api"))

    implementation(project(":kotlin_lib"))
    kapt(project(":stone_processor"))
}

