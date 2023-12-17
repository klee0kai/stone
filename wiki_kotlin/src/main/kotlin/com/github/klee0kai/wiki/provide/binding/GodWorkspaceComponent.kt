package com.github.klee0kai.wiki.provide.binding

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.test.mowgli.galaxy.Sun

@Component
interface GodWorkspaceComponent {

    fun sunSystem(): SunSystemModule

    fun sun(): Sun

}