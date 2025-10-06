plugins {
    java
}

group = "com.github.klee0kai.stone.test_feature.consulting"

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":test_feature:hr:api"))
    implementation(project(":test_feature:finance:api"))
    implementation(project(":test_feature:planning:api"))

    implementation(project(":test_feature:hr:impl"))
    implementation(project(":test_feature:finance:impl"))
    implementation(project(":test_feature:planning:impl"))

    implementation(project(":stone_lib"))
    annotationProcessor(project(":stone_processor"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

