package com.github.klee0kai.test.di.techfactory;

import com.github.klee0kai.stone.types.wrappers.IRef;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;

import javax.inject.Provider;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public interface ITechProviderComponent {

    LazyProvide<Battery> batteryLazy();

    IRef<Battery> batteryProviderIRef();

    PhantomProvide<Battery> batteryPhantomProvider();

    Provider<Battery> batteryProvider();

    SoftReference<Battery> batterySoft();

    WeakReference<Battery> batteryWeak();

    Ram ram();

    Ram ram(RamSize ramSize);

    OperationSystem phoneOs();

    OperationSystem phoneOs(PhoneOsType osType);

    OperationSystem phoneOs(PhoneOsType phoneOsType, PhoneOsVersion version);


}
