plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":kotlin_lib"))
    kapt(project(":stone_processor"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0-M2")
}
