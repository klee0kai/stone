package com.github.klee0kai.test_kotlin.di.base_comp.identifiers

class Company(
    val companyName: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Company
        if (companyName != other.companyName) return false
        return true
    }

    override fun hashCode(): Int {
        return companyName.hashCode()
    }
}