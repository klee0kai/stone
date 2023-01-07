package com.github.klee0kai.test.net

import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType
import java.util.*

class StoneApi private constructor() {
    var apiUrl: String? = null
    var token: String? = null
    var uuid = UUID.randomUUID()

    companion object {
        fun create(productType: ProductType?): StoneApi {
            val stoneApi = StoneApi()
            if (productType != null) when (productType) {
                ProductType.DEBUG -> stoneApi.apiUrl = "https://debug.org"
                ProductType.DEMO -> stoneApi.apiUrl = "https://demo.org"
                ProductType.RELEASE -> stoneApi.apiUrl = "https://release.org"
            }
            return stoneApi
        }
    }
}