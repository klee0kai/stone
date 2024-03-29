package com.github.klee0kai.test_kotlin.di.base_comp

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.Company
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.MonitorSize
import com.github.klee0kai.test_kotlin.tech.comp.DesktopComp
import com.github.klee0kai.test_kotlin.tech.comp.GameComp

@Component(
    identifiers = [MonitorSize::class, Company::class, KConnectType::class]
)
interface CompComponent {

    fun techModule(): TechModule

    fun inject(
        comp: DesktopComp,
        monitorSize: MonitorSize,
        monCompany: Company,
        kConnectType: KConnectType,
    )

    fun inject(
        comp: GameComp,
        lifeCycleOwner: StoneLifeCycleOwner?,
        monCompany: MonitorSize,
        kConnectType: KConnectType,
    )

    fun inject(
        lifeCycleOwner: StoneLifeCycleOwner?,
        comp: GameComp,
        monCompany: MonitorSize,
        kConnectType: KConnectType,
    )

}