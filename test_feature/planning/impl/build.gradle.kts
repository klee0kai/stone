plugins {
    java
}

group = "com.github.klee0kai.stone.test_feature.planning"

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":test_feature:planning:api"))
    implementation(project(":test_feature:finance:api"))
    implementation(project(":test_feature:hr:api"))

    implementation(project(":stone_lib"))
    annotationProcessor(project(":stone_processor"))
}

