plugins {
    id("java")
}

group = "com.github.klee0kai.stone.test_feature.hr"


dependencies {
    implementation(project(":test_feature:hr:api"))

    implementation(project(":stone_lib"))
    annotationProcessor(project(":stone_processor"))
}

