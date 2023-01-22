# Stone

[![](https://github.com/klee0kai/stone/actions/workflows/deploy_dev.yml/badge.svg)](https://github.com/klee0kai/stone/actions/workflows/deploy_dev.yml)
[![](https://img.shields.io/badge/license-GNU_GPLv3-blue.svg?style=flat-square)](./LICENCE.md)
[![](https://jitpack.io/v/klee0kai/stone.svg)](https://jitpack.io/#klee0kai/stone)

Library DI designed on weak references.

## Quick start

Add it in your root build.gradle at the end of repositories:

``` groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

``` groovy
dependencies {
    implementation 'com.github.klee0kai.stone:stone_lib:TAG'
    implementation 'com.github.klee0kai.stone:android_lib:TAG'
    annotationProcessor 'com.github.klee0kai.stone:stone_processor:TAG'
}
```

## License

```
Copyright (c) 2022 Andrey Kuzubov
```
