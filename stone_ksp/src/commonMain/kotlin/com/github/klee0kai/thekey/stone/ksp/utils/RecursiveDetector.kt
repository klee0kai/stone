package com.github.klee0kai.thekey.stone.ksp.utils

import java.util.*

/**
 * A tortoise is chasing the hare.
 * If the sequence is looped, then the hare will meet the tortoise on the next circle
 *
 * @param <T> sequence type
</T> */
class RecursiveDetector<T> {

    private val race = LinkedList<T?>()
    private var hareStep: Long = 0


    /**
     * Do next step.
     *
     * @param next next item in sequence
     * @return true if sequence is cycled
     */
    fun next(next: T?): Boolean {
        race.add(next)
        if (hareStep++ % 2 == 0L) {
            race.pollFirst()
        }
        if (race.size <= 2) return false

        val hare = next
        val turtle = race.first()
        return hare == turtle
    }

}
