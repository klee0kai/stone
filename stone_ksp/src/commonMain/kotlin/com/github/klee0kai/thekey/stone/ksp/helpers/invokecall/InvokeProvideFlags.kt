package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

data class InvokeProvideFlags(
    val provideObjectCached: Boolean = false,
    val provideBindInstance: Boolean = false,
)


fun InvokeProvideFlags.merge(
    invokeProvideFlags: InvokeProvideFlags,
) = InvokeProvideFlags(
    provideObjectCached = provideObjectCached || invokeProvideFlags.provideObjectCached,
    provideBindInstance = provideBindInstance || invokeProvideFlags.provideBindInstance,
)
