package com.github.klee0kai.test.data

import java.util.*

class StoneRepository {
    var uuid = UUID.randomUUID()
    var userId: String? = null

    constructor()
    constructor(userId: String?) {
        this.userId = userId
    }
}