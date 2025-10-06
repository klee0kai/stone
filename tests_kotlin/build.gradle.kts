plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":tests"))

    implementation(project(":kotlin_lib"))
    kapt(project(":stone_processor"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}


