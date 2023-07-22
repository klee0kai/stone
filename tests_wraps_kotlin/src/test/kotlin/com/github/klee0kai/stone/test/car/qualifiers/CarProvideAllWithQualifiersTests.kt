package com.github.klee0kai.stone.test.car.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent
import com.github.klee0kai.test.car.model.Car
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CarProvideAllWithQualifiersTests {
    @Test
    fun namedEmptyProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "named_empty" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car!!.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun namedAProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "named_a" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("reinforced", car!!.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "my_qualifier" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car!!.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "my_qualifier_with_string" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car!!.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringAProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "my_qualifier_a" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("reinforced", car!!.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringBProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "my_qualifier_b" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car!!.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierMultiProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "qualifier_multi" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car!!.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun carMyQualifierMultiA1ProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "qualifier_multi_a1" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car!!.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierMultiA2ProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "qualifier_multi_a2" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("reinforced", car!!.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun carMyQualifierMultiA2HardProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val cars = DI.allCars()

        //When
        val filtered: List<Car?> = cars!!.filter { it!!.qualifier == "qualifier_multi_a2_hard" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car!!.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun allCarsProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)

        //When
        val cars = DI.allCars()

        //Then
        assertEquals(14, cars!!.size)
        assertEquals(
            1,
            cars.count { it!!.qualifier == "my_qualifier_a" }
        )
        assertEquals(
            1,
            cars.count { it!!.qualifier == "qualifier_multi_a2" }
        )
        assertEquals(
            1,
            cars.count { it!!.qualifier == "qualifier_multi_a2_hard" }
        )
    }
}
