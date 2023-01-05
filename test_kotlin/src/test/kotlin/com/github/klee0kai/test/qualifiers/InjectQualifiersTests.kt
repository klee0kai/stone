package com.github.klee0kai.test.qualifiers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class InjectQualifiersTests {
    @Test
    fun simpleTest() {
        val app = QApp()
        app.startSimple()
        assertNull(app.stoneRepository!!.userId)
        assertNull(app.stoneApi!!.token)
        assertNull(app.stoneApi!!.apiUrl)
    }

    @Test
    fun debugTest() {
        val app = QApp()
        app.startDebug1()
        assertNull(app.stoneRepository!!.userId)
        assertNull(app.stoneApi!!.token)
        assertEquals("https://debug.org", app.stoneApi!!.apiUrl)
    }

    @Test
    fun demoTest() {
        val app = QApp()
        app.startDemo1()
        assertEquals("demo_user_id", app.stoneRepository!!.userId)
        assertEquals("demo_token", app.stoneApi!!.token)
        assertEquals("https://demo.org", app.stoneApi!!.apiUrl)
    }

    @Test
    fun releaseTest() {
        val app = QApp()
        app.startRelease()
        assertNull(app.stoneRepository!!.userId)
        assertEquals("release_token", app.stoneApi!!.token)
        assertEquals("https://release.org", app.stoneApi!!.apiUrl)
    }
}