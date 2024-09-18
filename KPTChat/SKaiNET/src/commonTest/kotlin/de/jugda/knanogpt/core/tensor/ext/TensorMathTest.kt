package de.jugda.knanogpt.core.tensor.ext

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import jp.co.qoncept.tensorkotlin.pow

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TensorTest {

    @Test
    fun testTransposeOnScalar() {
        val scalarTensor = Tensor(Shape(), doubleArrayOf(5.0))
        val exception = assertFailsWith<IllegalArgumentException> { scalarTensor.t() }
        assertEquals("Transpose is only implemented for 2D tensors.", exception.message)
    }

    @Test
    fun testTransposeOn1D() {
        val vectorTensor = Tensor(Shape(3), doubleArrayOf(1.0, 2.0, 3.0))
        val exception = assertFailsWith<IllegalArgumentException> { vectorTensor.t() }
        assertEquals("Transpose is only implemented for 2D tensors.", exception.message)
    }

    @Test
    fun testTransposeOn2D() {
        val matrixTensor = Tensor(Shape(2, 3), doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
        val transposedTensor = matrixTensor.t()
        assertEquals(Shape(3, 2), transposedTensor.shape)
        assertContentEquals(doubleArrayOf(1.0, 4.0, 2.0, 5.0, 3.0, 6.0), transposedTensor.elements)
    }

    @Test
    fun testFunction() {
        val x = Tensor(Shape(3), doubleArrayOf(1.0, 2.0, 3.0))
        val y = x * 2.0 + 3.0
        val z = y.pow(2.0)
        assertContentEquals(z.elements, doubleArrayOf(25.0, 49.0, 81.0))
    }

    @Test
    fun testTransposeScalar() {
        val tensor = Tensor(Shape())
        val transposed = tensor.transpose(0, 0)
        assertEquals(Shape(), transposed.shape, "Shape should remain unchanged for scalar")
    }

    @Test
    fun testTransposeVector() {
        val tensor = Tensor(Shape(5), doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0))
        val transposed = tensor.transpose(0, 0) // Transposing a vector doesn't change it
        assertContentEquals(
            doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
            transposed.elements,
            "Elements should remain unchanged for vector"
        )
    }

    @Test
    fun testTransposeMatrix() {
        val tensor = Tensor(Shape(2, 3), doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
        val transposed = tensor.transpose(0, 1)
        val expectedShape = Shape(3, 2)
        val expectedElements = doubleArrayOf(1.0, 4.0, 2.0, 5.0, 3.0, 6.0)
        assertEquals(expectedShape, transposed.shape, "Shape should be transposed for matrix")
        assertContentEquals(expectedElements, transposed.elements, "Elements should be rearranged for matrix")
    }
}
