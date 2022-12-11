package com.github.klee0kai.stone.test.qualifiers;

import com.github.klee0kai.test.qualifiers.QApp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class InjectQualifiersTests {

    @Test
    public void simpleTest() {
        QApp app = new QApp();
        app.startSimple();

        assertNull(app.stoneRepository.userId);
        assertNull(app.stoneApi.token);
        assertNull(app.stoneApi.apiUrl);
    }

    @Test
    public void debugTest() {
        QApp app = new QApp();
        app.startDebug1();

        assertNull(app.stoneRepository.userId);
        assertNull(app.stoneApi.token);
        assertEquals("https://debug.org", app.stoneApi.apiUrl);
    }

    @Test
    public void demoTest() {
        QApp app = new QApp();
        app.startDemo1();

        assertEquals("demo_user_id", app.stoneRepository.userId);
        assertEquals("demo_token", app.stoneApi.token);
        assertEquals("https://demo.org", app.stoneApi.apiUrl);
    }


    @Test
    public void releaseTest() {
        QApp app = new QApp();
        app.startRelease();

        assertNull(app.stoneRepository.userId);
        assertEquals("release_token", app.stoneApi.token);
        assertEquals("https://release.org", app.stoneApi.apiUrl);

    }


}
