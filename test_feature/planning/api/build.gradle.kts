plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "com.github.klee0kai.stone.test_feature.planning"

tasks.test {
    useJUnitPlatform()
}


