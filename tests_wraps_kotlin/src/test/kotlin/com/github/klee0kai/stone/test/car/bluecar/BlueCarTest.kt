package com.github.klee0kai.stone.test.car.bluecar

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.bestOf.blue.BlueCarComponent
import com.github.klee0kai.test.car.di.bestOf.both.BothCarComponent
import com.github.klee0kai.test.car.di.bestOf.red.RedCarComponent
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BlueCarTest {
    @BeforeEach
    fun init() {
        Bumper.createCount = 0
        Window.createCount = 0
        Wheel.createCount = 0
        Car.createCount = 0
    }

    @Test
    fun blueCarComponent() {
        //Given
        val DI = Stone.createComponent(BlueCarComponent::class.java)

        //When
        assertEquals(0, Car.createCount)
        val blueCar = DI.blueCar()!!.get()!!

        //Then
        assertEquals(1, Car.createCount)
        assertNotNull(blueCar.uuid)
        assertEquals(2, blueCar.bumpers!!.size, "Blue Car use list bumper")
        assertEquals(5, blueCar.wheels!!.size, "Blue Car use list wheel")
        assertEquals(4, blueCar.windows!!.size, "Blue Car use list wheel")
    }

    @Test
    fun createBlueCar() {
        //Given
        val DI = Stone.createComponent(BothCarComponent::class.java)

        //When
        assertEquals(0, Car.createCount)
        val blueCar = DI.blueCar().get()!!

        //Then
        assertEquals(1, Car.createCount)
        assertNotNull(blueCar.uuid)
        assertEquals(2, blueCar.bumpers!!.size, "Blue Car use list bumper")
        assertEquals(5, blueCar.wheels!!.size, "Blue Car use list wheel")
        assertEquals(4, blueCar.windows!!.size, "Blue Car use list wheel")
    }

    @Test
    fun redCarComponent() {
        //Given
        val DI = Stone.createComponent(RedCarComponent::class.java)

        //When
        assertEquals(0, Car.createCount)
        val redCar = DI.redCar().get()

        //Then
        assertEquals(1, Car.createCount)
        assertNotNull(redCar!!.uuid)
        assertEquals(1, redCar.bumpers!!.size, "Red Car use single bumper")
        assertEquals(1, redCar.wheels!!.size, "Red Car use single wheel")
        assertEquals(1, redCar.windows!!.size, "Red Car use single wheel")
    }

    @Test
    fun createRedCar() {
        //Given
        val DI = Stone.createComponent(BothCarComponent::class.java)

        //When
        assertEquals(0, Car.createCount)
        val redCar = DI.redCar().get()

        //Then
        assertEquals(1, Car.createCount)
        assertNotNull(redCar!!.uuid)
        assertEquals(1, redCar.bumpers!!.size, "Red Car use single bumper")
        assertEquals(1, redCar.wheels!!.size, "Red Car use single wheel")
        assertEquals(1, redCar.windows!!.size, "Red Car use single wheel")
    }
}
