package com.github.klee0kai.stone.test.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.Application;
import com.github.klee0kai.test.Context;
import com.github.klee0kai.test.gc.di.GComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class GcTests {


    @Test
    public void simpleGcTest() {
        Application app = Application.create();
        Context context = Context.create();

        GComponent DI = Stone.createComponent(GComponent.class);
        DI.bind(app, context);

        //using items not collect by gc
        DI.gcAll();

        assertNotNull(DI.app().app());
        assertNotNull(DI.app().context());

        //not using items collect by gc
        app = null;
        context = null;
        DI.gcAll();
        assertNull(DI.app().app());
        assertNull(DI.app().context());
    }

    @Test
    public void appGcScopeTest() {
        Application app = Application.create();
        Context context = Context.create();

        GComponent DI = Stone.createComponent(GComponent.class);
        DI.bind(app, context);

        //using items not collect by gc
        DI.gcApp();

        assertNotNull(DI.app().app());
        assertNotNull(DI.app().context());

        //not using items collect by gc
        app = null;
        context = null;
        DI.gcApp();
        assertNull(DI.app().app());
        assertNotNull(DI.app().context());
    }


    @Test
    public void contextGcScopeTest() {
        Application app = Application.create();
        Context context = Context.create();

        GComponent DI = Stone.createComponent(GComponent.class);
        DI.bind(app, context);

        //using items not collect by gc
        DI.gcContext();

        assertNotNull(DI.app().app());
        assertNotNull(DI.app().context());

        //not using items collect by gc
        app = null;
        context = null;
        DI.gcContext();
        assertNotNull(DI.app().app());
        assertNull(DI.app().context());
    }

    @Test
    public void multiGcScopeTest() {
        Application app = Application.create();
        Context context = Context.create();

        GComponent DI = Stone.createComponent(GComponent.class);
        DI.bind(app, context);

        //using items not collect by gc
        DI.gcAppAndContext();

        assertNotNull(DI.app().app());
        assertNotNull(DI.app().context());

        //not using items collect by gc
        app = null;
        context = null;
        DI.gcAppAndContext();
        assertNull(DI.app().app());
        assertNull(DI.app().context());
    }
}
