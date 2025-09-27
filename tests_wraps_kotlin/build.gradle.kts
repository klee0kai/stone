plugins {
    id("org.jetbrains.kotlin.jvm").version("1.7.0")
    id("org.jetbrains.kotlin.kapt").version("1.7.21")
}

dependencies {
    implementation(project(":tests"))

    implementation(project(":kotlin_lib"))
    kapt(project(":stone_processor"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

