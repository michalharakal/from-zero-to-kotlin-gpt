package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import kotlin.test.*
import kotlin.math.abs

class TensorCatTest {

    private fun assertTensorClose(expected: Tensor, actual: Tensor, atol: Double = 1e-6) {
        assertTrue { expected.shape.dimensions.contentEquals(actual.shape.dimensions) }
        assertTrue { expected.elements.size == actual.elements.size }
        expected.elements.zip(actual.elements).forEach { (exp, act) ->
            assertTrue { abs(exp - act) < atol }
        }
    }

    @Test
    fun testLayerNormScalar() {
        val input = Tensor(Shape(1), doubleArrayOf(3.0))
        val layerNorm = LayerNorm(1)
        val result = layerNorm.forward(input)

        // For a single value, normalization should result in 0.0
        assertTensorClose(Tensor(Shape(1), doubleArrayOf(0.0)), result)
    }

    @Test
    fun testLayerNormVector() {
        val input = Tensor(Shape(3), doubleArrayOf(1.0, 2.0, 3.0))
        val layerNorm = LayerNorm(3)
        val result = layerNorm.forward(input)

        // Expected results are calculated manually
        assertTensorClose(Tensor(Shape(3), doubleArrayOf(-1.22474487, 0.0, 1.22474487)), result)
    }

    @Test
    fun testLayerNormMatrix() {
        val input = Tensor(Shape(2, 3), doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
        val layerNorm = LayerNorm(3)
        val result = layerNorm.forward(input)

        // Expected results for each sub-tensor (row) are calculated manually
        assertTensorClose(
            Tensor(
                Shape(2, 3),
                doubleArrayOf(-1.22474487, 0.0, 1.22474487, -1.22474487, 0.0, 1.22474487)
            ), result
        )
    }
}
