# Stone

[![](https://github.com/klee0kai/stone/actions/workflows/deploy_dev.yml/badge.svg)](https://github.com/klee0kai/stone/actions/workflows/deploy_dev.yml)
[![](https://img.shields.io/badge/license-GNU_GPLv3-blue.svg?style=flat-square)](./LICENCE.md)
[![](https://jitpack.io/v/klee0kai/stone.svg)](https://jitpack.io/#klee0kai/stone)

A DI library designed for jvm programs, working on the basis of various types of links: weak, soft, strong.
The main goal of the library is to decouple DI from the need to work with the life cycles of application components,
transfer the work of the life cycle to the components themselves from the life cycle

Read more about the idealogy solution on [wiki](https://github.com/klee0kai/stone/wiki)

## Quick start

Quick start on [java](https://github.com/klee0kai/stone/wiki/java_start).
For [kotlin](https://github.com/klee0kai/stone/wiki/kotlin_start).
All examples are gradle oriented.

The library is supplied via the `jitpack` repository.

```kotlin 
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://jitpack.io")
    }
}
```

In the project module, import dependencies

```kotlin
dependencies {
    // stone
    implementation("com.github.klee0kai.stone:stone_lib:1.0.3")
    kapt("com.github.klee0kai.stone:stone_processor:1.0.3")
}
```

Simple usage boils down to declaring a grant object

```
@Module
abstract class PlanetsModule {

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun earth(): Earth 

}

@Component
interface PlanetsComponent {

    fun planets(): PlanetsModule

}
```

And further use.

```kotlin 
val DI: PlanetsComponent = Stone.createComponent(PlanetsComponent::class.java)
fun main(args: Array<String>) {
    val earth = DI.planets().earth()
}
```

## Introduction

The philosophy of the library is that it does not hold any objects by itself.
However, if the object is not destroyed in the GC, then it can be reused.
Objects in Stone are held only by the consumers of these very objects.
If the consumer stops using the object, the object may be destroyed.

This approach allows you to get rid of intermediaries working with DI,
additional integrations for example in navigation.
It also saves the application from using various managers 
and custom object providers in areas where DI usually couldn’t cope.

DI should be one factory that provides the creation of all the main application objects.

## Features

### Blurred scopes

The library relieves the developer of the need to describe the scope of using a local singleton in a project.
The life of each object is determined only by its consumers, who retain it in memory.

### Memory hot swap

In Stone, you can gracefully replace object factories at runtime.
Replace bindings and extend the DI component.
All this allows you to implement dynamic application features.
Flexibly develop UI testing using substitute mock data.

### Qualifiers and Identifiers

Unlike conventional DI implementations, Stone supports not only qualifiers, but also object identifiers.
Identifiers allow you to create and distinguish more and more local singletons in an unlimited number.
And blurred scopes ensure that instances are properly cleaned as they are released.

### Gradual warm-up

The library supports the use of provider wrappers.
This allows you to lazily initialize class instances, or on the 2nd thread.
You can also implement your own wrapper to work through kotlin delegates, or to implement initialization in coroutine scopes.

## License

```
Copyright (c) 2024 Andrey Kuzubov
```
