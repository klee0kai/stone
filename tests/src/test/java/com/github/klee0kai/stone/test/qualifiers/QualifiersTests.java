package com.github.klee0kai.stone.test.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.data.StoneRepository;
import com.github.klee0kai.test.net.StoneApi;
import com.github.klee0kai.test.qualifiers.di.QTestComponent;
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType;
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token;
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class QualifiersTests {

    @Test
    public void singleQualifiersTest() {
        QTestComponent DI = Stone.createComponent(QTestComponent.class);

        StoneRepository stoneRepository1 = DI.data().stoneRepository(new UserId("userId1"));
        StoneRepository stoneRepositoryNull = DI.data().stoneRepository(new UserId(null));
        StoneRepository stoneRepository2 = DI.data().stoneRepository(new UserId("user2"));


        //check created components
        assertNotNull(stoneRepository1);
        assertNotNull(stoneRepositoryNull);
        assertNotNull(stoneRepository2);

        //check created components was different
        assertNotEquals(stoneRepository1.uuid, stoneRepositoryNull.uuid);
        assertNotEquals(stoneRepositoryNull.uuid, stoneRepository2.uuid);

        //check created components got create arguments
        assertEquals(stoneRepository1.userId, "userId1");
        assertNull(stoneRepositoryNull.userId);
        assertEquals(stoneRepository2.userId, "user2");


        StoneRepository stoneRepository1_cached = DI.data().stoneRepository(new UserId("userId1"));
        StoneRepository stoneRepositoryNull_cached = DI.data().stoneRepository(new UserId(null));
        StoneRepository stoneRepository2_cached = DI.data().stoneRepository(new UserId("user2"));

        // check cached components
        assertEquals(stoneRepository1_cached.uuid, stoneRepository1.uuid);
        assertEquals(stoneRepositoryNull_cached.uuid, stoneRepositoryNull.uuid);
        assertEquals(stoneRepository2_cached.uuid, stoneRepository2.uuid);
    }

    @Test
    public void enumQualifiersFactoryTest() {
        QTestComponent DI = Stone.createComponent(QTestComponent.class);

        StoneApi debugApi = DI.inet().stoneApiFactory(ProductType.DEBUG);
        StoneApi demoApi = DI.inet().stoneApiFactory(ProductType.DEMO);
        StoneApi demo2Api = DI.inet().stoneApiFactory(ProductType.DEMO);
        StoneApi demo3Api = DI.inet().stoneApiFactory(ProductType.DEMO);
        StoneApi releaseApi = DI.inet().stoneApiFactory(ProductType.RELEASE);
        StoneApi nullApi = DI.inet().stoneApiFactory(null);
        StoneApi null2Api = DI.inet().stoneApiFactory(null);

        assertEquals(debugApi.apiUrl, "https://debug.org");
        assertEquals(demoApi.apiUrl, "https://demo.org");
        assertEquals(demo3Api.apiUrl, "https://demo.org");
        assertNull(null2Api.apiUrl);

        List<StoneApi> apis = List.of(debugApi, demoApi, demo2Api, demo3Api, releaseApi, nullApi, null2Api);
        for (int i = 0; i < apis.size(); i++)
            for (int j = i + 1; j < apis.size(); j++)
                assertNotEquals(apis.get(i), apis.get(j));

    }


    @Test
    public void enumQualifiersCachedTest() {
        QTestComponent DI = Stone.createComponent(QTestComponent.class);

        StoneApi debugApi = DI.inet().stoneApiSoft(ProductType.DEBUG);
        StoneApi demoApi = DI.inet().stoneApiSoft(ProductType.DEMO);
        StoneApi demo2Api = DI.inet().stoneApiSoft(ProductType.DEMO);
        StoneApi demo3Api = DI.inet().stoneApiSoft(ProductType.DEMO);
        StoneApi releaseApi = DI.inet().stoneApiSoft(ProductType.RELEASE);
        StoneApi nullApi = DI.inet().stoneApiSoft(null);
        StoneApi null2Api = DI.inet().stoneApiSoft(null);

        assertEquals(debugApi.apiUrl, "https://debug.org");
        assertEquals(demoApi.apiUrl, "https://demo.org");
        assertEquals(demo3Api.apiUrl, "https://demo.org");
        assertNull(null2Api.apiUrl);


        List<StoneApi> apis = List.of(debugApi, demoApi, releaseApi, nullApi);
        for (int i = 0; i < apis.size(); i++)
            for (int j = i + 1; j < apis.size(); j++)
                assertNotEquals(apis.get(i), apis.get(j));

        assertEquals(demoApi.uuid, demo2Api.uuid);
        assertEquals(demo2Api.uuid, demo3Api.uuid);
        assertEquals(nullApi.uuid, null2Api.uuid);
    }


    @Test
    public void multiQualifiersFactoryTest() {
        QTestComponent DI = Stone.createComponent(QTestComponent.class);

        StoneApi debugApi = DI.inet().userApiFactory(ProductType.DEBUG, new Token("token"));
        StoneApi demoApi = DI.inet().userApiFactory(ProductType.DEMO, new Token("token"));
        StoneApi demo2Api = DI.inet().userApiFactory(ProductType.DEMO, new Token("token"));
        StoneApi demo3Api = DI.inet().userApiFactory(ProductType.DEMO, new Token("token1"));
        StoneApi releaseApi = DI.inet().userApiFactory(ProductType.RELEASE, new Token(null));
        StoneApi nullApi = DI.inet().userApiFactory(null, new Token("token"));
        StoneApi null2Api = DI.inet().userApiFactory(null, new Token("token"));
        StoneApi null3Api = DI.inet().userApiFactory(null, new Token("token2"));

        // check product type arg
        assertEquals(debugApi.apiUrl, "https://debug.org");
        assertEquals(demoApi.apiUrl, "https://demo.org");
        assertEquals(demo3Api.apiUrl, "https://demo.org");
        assertNull(null2Api.apiUrl);
        // check token arg
        assertEquals(demo2Api.token, "token");
        assertEquals(demo3Api.token, "token1");
        assertNull(releaseApi.token);
        assertEquals(null3Api.token, "token2");

        //check factory creating
        List<StoneApi> apis = List.of(debugApi, demoApi, demo2Api, demo3Api, releaseApi, nullApi, null2Api, null3Api);
        for (int i = 0; i < apis.size(); i++)
            for (int j = i + 1; j < apis.size(); j++)
                assertNotEquals(apis.get(i), apis.get(j));

    }


    @Test
    public void multiQualifiersCacheTest() {
        QTestComponent DI = Stone.createComponent(QTestComponent.class);

        StoneApi debugApi = DI.inet().userApiStrong(ProductType.DEBUG,  new Token("token"));
        StoneApi demoApi = DI.inet().userApiStrong(ProductType.DEMO,  new Token("token"));
        StoneApi demo2Api = DI.inet().userApiStrong(ProductType.DEMO,  new Token("token"));
        StoneApi demo3Api = DI.inet().userApiStrong(ProductType.DEMO,  new Token("token1"));
        StoneApi releaseApi = DI.inet().userApiStrong(ProductType.RELEASE, new Token( null));
        StoneApi nullApi = DI.inet().userApiStrong(null,  new Token("token"));
        StoneApi null2Api = DI.inet().userApiStrong(null,  new Token("token"));
        StoneApi null3Api = DI.inet().userApiStrong(null,  new Token("token2"));
        StoneApi null4Api = DI.inet().userApiStrong(null,  new Token(null));
        StoneApi null5Api = DI.inet().userApiStrong(null,  new Token(null));

        // check product type arg
        assertEquals(debugApi.apiUrl, "https://debug.org");
        assertEquals(demoApi.apiUrl, "https://demo.org");
        assertEquals(demo3Api.apiUrl, "https://demo.org");
        assertNull(null2Api.apiUrl);
        // check token arg
        assertEquals(demo2Api.token, "token");
        assertEquals(demo3Api.token, "token1");
        assertNull(releaseApi.token);
        assertEquals(null3Api.token, "token2");

        //check different component creating
        List<StoneApi> apis = List.of(debugApi, demoApi, demo3Api, releaseApi, nullApi, null3Api);
        for (int i = 0; i < apis.size(); i++)
            for (int j = i + 1; j < apis.size(); j++)
                assertNotEquals(apis.get(i), apis.get(j));

        //check cached components
        assertEquals(demoApi.uuid, demo2Api.uuid);
        assertEquals(nullApi.uuid, null2Api.uuid);
        assertEquals(null4Api.uuid, null5Api.uuid);
    }

}
