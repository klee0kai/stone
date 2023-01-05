package com.github.klee0kai.test.qualifiers.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.test.qualifiers.QApp
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId

@Component(qualifiers = [ProductType::class, Token::class, UserId::class])
interface QTestComponent {
    fun inet(): QInetModule
    fun data(): QDataModule
    fun inject(qApp: QApp)
    fun inject(qApp: QApp, debug: ProductType)
    fun inject(demo: ProductType, demoUserId: UserId, demoToke: Token, qApp: QApp)
    fun inject(qApp: QApp?, release_token: Token?, release: ProductType?)
}