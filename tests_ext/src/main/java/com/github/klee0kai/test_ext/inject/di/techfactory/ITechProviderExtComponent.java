package com.github.klee0kai.test_ext.inject.di.techfactory;

import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test_ext.inject.di.techfactory.qualifiers.Frequency;
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram;

public interface ITechProviderExtComponent {

    Ram ramExt();

    Ram ramExt(RamSize ramSize);

    Ram ramExt(RamSize ramSize, Frequency frequency);


    DDR3Ram ramDDr3Ext();

    DDR3Ram ramDdr3Ext(RamSize ramSize);

    DDR3Ram ramDDr3Ext(RamSize ramSize, Frequency frequency);

}
