plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.ksp)
}

group = "com.github.klee0kai.stone.test_feature.planning"

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":test_feature:planning:api"))
    implementation(project(":test_feature:finance:api"))
    implementation(project(":test_feature:hr:api"))

    implementation(project(":stone_multiplatform"))
    ksp(project(":stone_ksp"))
}

