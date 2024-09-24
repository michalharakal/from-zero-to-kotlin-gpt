package org.skainet.activations

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import kotlin.math.exp
import kotlin.test.Test

import kotlin.test.assertContentEquals

class SoftmaxTest {
    @Test
    fun testSoftmax() {
        val logits = Tensor(Shape(3), doubleArrayOf(2.0, 1.0, 0.5))
        val softmaxOutput = logits.softmax().elements
        val expectedOutput = doubleArrayOf(
            exp(2.0) / (exp(2.0) + exp(1.0) + exp(0.5)),
            exp(1.0) / (exp(2.0) + exp(1.0) + exp(0.5)),
            exp(0.5) / (exp(2.0) + exp(1.0) + exp(0.5))
        )

        assertContentEquals(expectedOutput, softmaxOutput)
    }

    @Test
    fun testSoftmaxSingleElement() {
        val singleLogit = Tensor(Shape(1), doubleArrayOf(3.0))
        val softmaxOutput = singleLogit.softmax().elements
        val expectedOutput = doubleArrayOf(1.0) // Softmax of a single value should be 1

        assertContentEquals(expectedOutput, softmaxOutput)
    }
}