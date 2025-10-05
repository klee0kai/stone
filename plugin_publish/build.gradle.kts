plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins.register("stone-publish") {
        id = "com.github.klee0kai.stone.publish"
        implementationClass = "com.github.klee0kai.stone.publish.StonePublishPlugin"
    }
}
