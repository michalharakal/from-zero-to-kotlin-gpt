package de.jugda.knanogpt.core.tensor.ext


import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


class TensorAdditionTest {
    @Test
    fun testSimpleVectorAddition() {
        // Single element tensor should correctly calculate product as the element itself.
        val A = Tensor(Shape(1), listOf(1, 2, 3, 4).map { it.toDouble() }.toDoubleArray())
        val B = Tensor(Shape(1), listOf(5, 6, 7, 8).map { it.toDouble() }.toDoubleArray())
        val C = Tensor(Shape(1), listOf(6, 8, 10, 12).map { it.toDouble() }.toDoubleArray())
        assertContentEquals(
            C.elements,
            (A + B).elements,
            "Product of a simple tensor addition should be a tensor with elements 6, 8, 10, 12"
        )
        assertEquals(A.shape, C.shape)
    }

    @Test
    fun testSimpleMatrixAddition() {
        // Single element tensor should correctly calculate product as the element itself.
        val A = Tensor(Shape(2, 2), listOf(1, 2, 3, 4).map { it.toDouble() }.toDoubleArray())
        val B = Tensor(Shape(2, 2), listOf(5, 6, 7, 8).map { it.toDouble() }.toDoubleArray())
        val C = Tensor(Shape(2, 2), listOf(6, 8, 10, 12).map { it.toDouble() }.toDoubleArray())
        assertContentEquals(
            C.elements,
            (A + B).elements,
            "Product of a simple tensor addition should be a tensor with elements 6, 8, 10, 12"
        )
        assertEquals(A.shape, C.shape)
    }

}