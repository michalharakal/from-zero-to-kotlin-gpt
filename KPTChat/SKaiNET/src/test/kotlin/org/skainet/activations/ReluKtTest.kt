package org.skainet.activations

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import kotlin.test.Test

import kotlin.test.assertContentEquals

class TensorTest {

    @Test
    fun testReluPositive() {
        val inputTensor = Tensor(Shape(3), doubleArrayOf(1.0, 2.0, 3.0))
        val expected = doubleArrayOf(1.0, 2.0, 3.0)
        val result = relu(inputTensor).elements
        assertContentEquals(expected, result, "ReLU should not change positive values")
    }

    @Test
    fun testReluNegative() {
        val inputTensor = Tensor(Shape(3), doubleArrayOf(-1.0, -2.0, -3.0))
        val expected = doubleArrayOf(0.0, 0.0, 0.0)
        val result = relu(inputTensor).elements
        assertContentEquals(expected, result, "ReLU should change negative values to 0")
    }

    @Test
    fun testReluZero() {
        val inputTensor = Tensor(Shape(3), doubleArrayOf(0.0, 0.0, 0.0))
        val expected = doubleArrayOf(0.0, 0.0, 0.0)
        val result = relu(inputTensor).elements
        assertContentEquals(expected, result, "ReLU should not change zero values")
    }

    @Test
    fun testReluMixed() {
        val inputTensor = Tensor(Shape(2, 3), doubleArrayOf(-1.0, 0.0, 1.0, -2.0, 2.0, 3.0))
        val expected = doubleArrayOf(0.0, 0.0, 1.0, 0.0, 2.0, 3.0)
        val result = relu(inputTensor).elements
        assertContentEquals(expected, result, "ReLU should correctly apply to mixed values")
    }

    @Test
    fun testRelu3D() {
        val inputTensor = Tensor(Shape(2, 2, 2), doubleArrayOf(-1.0, 2.0, -3.0, 4.0, -5.0, 6.0, -7.0, 8.0))
        val expected = doubleArrayOf(0.0, 2.0, 0.0, 4.0, 0.0, 6.0, 0.0, 8.0)
        val result = relu(inputTensor).elements
        assertContentEquals(expected, result, "ReLU should correctly apply to 3D tensor with mixed values")
    }
}
