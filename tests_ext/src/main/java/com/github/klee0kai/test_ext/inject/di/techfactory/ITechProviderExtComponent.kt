package com.github.klee0kai.test_ext.inject.di.techfactory

import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.components.Ram
import com.github.klee0kai.test_ext.inject.di.techfactory.identifiers.Frequency
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram
import javax.inject.Named

interface ITechProviderExtComponent {

    fun ramExt(): Ram?

    fun ramExt(ramSize: RamSize?): Ram?

    fun ramExt(ramSize: RamSize?, frequency: Frequency?): Ram


    @Named
    fun ramDDr3Ext(): DDR3Ram?

    @Named("size")
    fun ramDdr3Ext(ramSize: RamSize?): DDR3Ram

    @Named("size-frequency")
    fun ramDDr3Ext(ramSize: RamSize?, frequency: Frequency?): DDR3Ram

}
