package de.jugda.knanogpt.core.tensor.ext


import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor

import kotlin.test.Test
import kotlin.test.assertContentEquals

class TensorTrilTest {

    @Test
    fun testTrilScalar() {
        val scalar = Tensor(Shape(), doubleArrayOf(5.0))
        val result = scalar.tril()
        assertContentEquals(doubleArrayOf(5.0), result.elements)
    }

    @Test
    fun testTrilVector() {
        val vector = Tensor(Shape(3), doubleArrayOf(1.0, 2.0, 3.0))
        val result = vector.tril()
        assertContentEquals(doubleArrayOf(1.0, 2.0, 3.0), result.elements)
    }

    @Test
    fun testTrilMatrix() {
        val matrix = Tensor(Shape(3, 3), doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0))
        val result = matrix.tril()
        assertContentEquals(doubleArrayOf(1.0, 0.0, 0.0, 4.0, 5.0, 0.0, 7.0, 8.0, 9.0), result.elements)
    }
}
