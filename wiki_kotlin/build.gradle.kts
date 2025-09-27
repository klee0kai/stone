plugins {
    id("org.jetbrains.kotlin.jvm").version("1.7.0")
    id("org.jetbrains.kotlin.kapt").version("1.7.21")
}


dependencies {
    implementation(project(":tests"))
    implementation(project(":test_feature:finance:api"))

    implementation(project(":kotlin_lib"))
    kapt(project(":stone_processor"))

}

