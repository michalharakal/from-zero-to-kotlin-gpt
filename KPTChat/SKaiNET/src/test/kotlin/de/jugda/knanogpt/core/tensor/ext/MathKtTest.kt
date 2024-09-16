package de.jugda.knanogpt.core.tensor.ext

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor

import kotlin.test.Test
import kotlin.test.assertEquals


class TensorProdTest {

    @Test
    fun testProdEmptyTensor() {
        // Empty tensor should have a product of 1.
        val tensor = Tensor(Shape(), doubleArrayOf())
        assertEquals(1.0, tensor.prod(), 0.0, "Product of an empty tensor should be 1")
    }


    @Test
    fun testProdSingleElement() {
        // Single element tensor should correctly calculate product as the element itself.
        val tensor = Tensor(Shape(1), 3.0)
        assertEquals(3.0, tensor.prod(), 0.0, "Product of a single-element tensor should be the element itself")
    }

    @Test
    fun testProdMultipleElements() {
        // Explicitly creating a tensor with two elements, each being 4.0.
        val tensor = Tensor(Shape(2), doubleArrayOf(4.0, 4.0))
        assertEquals(16.0, tensor.prod(), 0.0, "Product of a tensor with two elements 4 should be 16")
    }

    @Test
    fun testProdDifferentDimensions() {
        // Creating a 2x2 tensor with all elements as 2.0 explicitly.
        val tensor = Tensor(Shape(2, 2), doubleArrayOf(2.0, 2.0, 2.0, 2.0))
        assertEquals(16.0, tensor.prod(), 0.0, "Product of a 2x2 tensor with all elements 2 should be 16")
    }

    @Test
    fun testProdWithZero() {
        // Tensor with a zero element will result in a product of zero.
        val tensor = Tensor(Shape(3), doubleArrayOf(0.0, 2.0, 2.0))
        assertEquals(0.0, tensor.prod(), 0.0, "Product of a tensor containing a zero should be 0")
    }

    @Test
    fun testProdWithNegativeElements() {
        // A tensor with negative elements.
        val tensor = Tensor(Shape(2), doubleArrayOf(-3.0, -3.0))
        assertEquals(9.0, tensor.prod(), 0.0, "Product of a tensor with all elements -3 should be 9")
    }

    @Test
    fun testProdLargeTensor() {
        // For a large tensor where all elements are 1, initializing with the single value constructor is fine.
        val tensor = Tensor(Shape(10), 1.0)
        assertEquals(1.0, tensor.prod(), 0.0, "Product of a tensor with all elements 1 and shape of 10 should be 1")
    }
}
