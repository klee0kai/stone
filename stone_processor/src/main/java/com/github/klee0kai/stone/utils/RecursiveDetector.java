package com.github.klee0kai.stone.utils;

import java.util.LinkedList;
import java.util.Objects;

/**
 * A tortoise is chasing the hare.
 * If the sequence is looped, then the hare will meet the tortoise on the next circle
 *
 * @param <T> sequence type
 */
public class RecursiveDetector<T> {

    private final LinkedList<T> race = new LinkedList<>();
    private long hareStep = 0;


    /**
     * Do next step.
     *
     * @param next next item in sequence
     * @return true if sequence is cycled
     */
    public boolean next(T next) {
        race.add(next);
        if (hareStep++ % 2 == 0) {
            race.pollFirst();
        }
        if (race.size() <= 2) return false;

        T hare = next;
        T turtle = race.getFirst();
        return Objects.equals(hare, turtle);
    }


}
