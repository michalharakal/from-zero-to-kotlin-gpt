package de.jugda.knanogpt.core.tensor.ext

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor


import org.junit.Assert.assertArrayEquals
import org.junit.Test

class TensorMaskedFillTest {

    @Test
    fun testMaskedFillScalar() {
        val tensor = Tensor(Shape(), doubleArrayOf(5.0))
        val mask = Tensor(Shape(), doubleArrayOf(1.0)) // Mask with a single true value
        val filled = tensor.maskedFill(mask, -1.0)

        assertArrayEquals(doubleArrayOf(-1.0), filled.elements, 0.0)
    }

    @Test
    fun testMaskedFillVector() {
        val tensor = Tensor(Shape(3), doubleArrayOf(1.0, 2.0, 3.0))
        val mask = Tensor(Shape(3), doubleArrayOf(0.0, 1.0, 0.0)) // Mask the second element
        val filled = tensor.maskedFill(mask, -1.0)

        assertArrayEquals(doubleArrayOf(1.0, -1.0, 3.0), filled.elements, 0.0)
    }

    @Test
    fun testMaskedFillMatrix() {
        val tensor = Tensor(Shape(2, 2), doubleArrayOf(1.0, 2.0, 3.0, 4.0))
        val mask = Tensor(Shape(2, 2), doubleArrayOf(0.0, 1.0, 1.0, 0.0)) // Mask second and third elements
        val filled = tensor.maskedFill(mask, -1.0)

        assertArrayEquals(doubleArrayOf(1.0, -1.0, -1.0, 4.0), filled.elements, 0.0)
    }
}
