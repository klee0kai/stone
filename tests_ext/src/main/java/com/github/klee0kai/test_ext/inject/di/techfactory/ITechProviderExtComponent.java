package com.github.klee0kai.test_ext.inject.di.techfactory;

import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test_ext.inject.di.techfactory.identifiers.Frequency;
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram;

import javax.inject.Named;

public interface ITechProviderExtComponent {

    Ram ramExt();

    Ram ramExt(RamSize ramSize);

    Ram ramExt(RamSize ramSize, Frequency frequency);


    @Named()
    DDR3Ram ramDDr3Ext();

    @Named("size")
    DDR3Ram ramDdr3Ext(RamSize ramSize);

    @Named("size-frequency")
    DDR3Ram ramDDr3Ext(RamSize ramSize, Frequency frequency);

}
