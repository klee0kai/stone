package com.github.klee0kai.test.net;

import com.github.klee0kai.test.qualifiers.ProductType;

import java.util.UUID;

public class StoneApi {

    public String apiUrl = null;

    public UUID uuid = UUID.randomUUID();

    public static StoneApi create(ProductType productType) {
        StoneApi stoneApi = new StoneApi();
        switch (productType) {
            case DEBUG -> {
                stoneApi.apiUrl = "https://debug.org";
            }
            case DEMO -> {
                stoneApi.apiUrl = "https://demo.org";
            }
            case RELEASE -> {
                stoneApi.apiUrl = "https://release.org";
            }
        }
        return stoneApi;

    }


    private StoneApi() {

    }

}
