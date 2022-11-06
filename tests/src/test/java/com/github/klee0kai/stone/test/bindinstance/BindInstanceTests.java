package com.github.klee0kai.stone.test.bindinstance;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.Application;
import com.github.klee0kai.test.Context;
import com.github.klee0kai.test.bindinstance.di.BindInstanceTextComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BindInstanceTests {


    @Test
    public void simpleInitTest() {
        Application application = Application.create();
        Context context = Context.create();
        BindInstanceTextComponent DI = Stone.createComponent(BindInstanceTextComponent.class);
        DI.bind(application, context);

        assertNotEquals(DI.appModule().application().uuid, DI.appModule().context().uuid);
    }

    @Test
    public void simpleInitTest2() {
        Application application = Application.create();
        Context context = Context.create();
        BindInstanceTextComponent DI = Stone.createComponent(BindInstanceTextComponent.class);
        DI.bind(context, application);
        assertNotEquals(DI.appModule().application().uuid, DI.appModule().context().uuid);
    }


    @Test
    public void simpleInitTest3() {
        Application application = Application.create();
        Context context = Context.create();
        BindInstanceTextComponent DI = Stone.createComponent(BindInstanceTextComponent.class);
        DI.bind(application);

        assertNull(DI.appModule().context());
        assertNotNull(DI.appModule().application());
    }

    @Test
    public void simpleInitTest4() {
        Application application = Application.create();
        Context context = Context.create();
        BindInstanceTextComponent DI = Stone.createComponent(BindInstanceTextComponent.class);
        DI.bind(context);

        assertNotNull(DI.appModule().context());
        assertNull(DI.appModule().application());
    }

}
