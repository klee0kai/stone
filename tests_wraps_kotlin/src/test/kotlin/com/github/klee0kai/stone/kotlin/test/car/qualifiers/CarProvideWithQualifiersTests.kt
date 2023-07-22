package com.github.klee0kai.stone.kotlin.test.car.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CarProvideWithQualifiersTests {
    @Test
    fun namedEmptyProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carNamedEmpty()

        //Then
        assertEquals("named_empty", car!!.qualifier)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun namedAProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carNameA()

        //Then
        assertEquals("named_a", car!!.qualifier)
        assertEquals("reinforced", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifier()

        //Then
        assertEquals("my_qualifier", car!!.qualifier)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifierString()

        //Then
        assertEquals("my_qualifier_with_string", car!!.qualifier)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringAProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifierStringA()

        //Then
        assertEquals("my_qualifier_a", car!!.qualifier)
        assertEquals("reinforced", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringBProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifierStringB()

        //Then
        assertEquals("my_qualifier_b", car!!.qualifier)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierMultiProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifierMulti()

        //Then
        assertEquals("qualifier_multi", car!!.qualifier)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun carMyQualifierMultiA1ProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifierMultiA1()

        //Then
        assertEquals("qualifier_multi_a1", car!!.qualifier)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierMultiA2ProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifierMultiA2()

        //Then
        assertEquals("qualifier_multi_a2", car!!.qualifier)
        assertEquals("reinforced", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun carMyQualifierMultiA2HardProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val car = DI.carMyQualifierMultiA2Hard()

        //Then
        assertEquals("qualifier_multi_a2_hard", car!!.qualifier)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }
}
