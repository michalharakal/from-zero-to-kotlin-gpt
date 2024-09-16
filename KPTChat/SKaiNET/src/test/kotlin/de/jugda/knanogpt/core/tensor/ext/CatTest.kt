package de.jugda.knanogpt.core.tensor.ext


import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class TensorCatTest {

    @Test
    fun testCatScalars() {
        val tensor1 = Tensor(Shape(), doubleArrayOf(1.0))
        val tensor2 = Tensor(Shape(), doubleArrayOf(2.0))
        val result = cat(listOf(tensor1, tensor2), 0)
        assertArrayEquals(doubleArrayOf(1.0, 2.0), result.elements, 0.0)
    }

    @Test
    fun testCatVectors() {
        val tensor1 = Tensor(Shape(3), doubleArrayOf(1.0, 2.0, 3.0))
        val tensor2 = Tensor(Shape(3), doubleArrayOf(4.0, 5.0, 6.0))
        val result = cat(listOf(tensor1, tensor2), 0)
        assertArrayEquals(doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), result.elements, 0.0)
    }

    @Test
    fun testCatMatrices() {
        val tensor1 = Tensor(Shape(2, 3), doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
        val tensor2 = Tensor(Shape(2, 3), doubleArrayOf(7.0, 8.0, 9.0, 10.0, 11.0, 12.0))
        val result = cat(listOf(tensor1, tensor2), 0)
        assertArrayEquals(doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0), result.elements, 0.0)
    }
}
