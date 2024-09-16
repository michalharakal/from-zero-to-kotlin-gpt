package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

import kotlin.test.assertTrue

class EmbeddingTest {

    private fun assertTensorEquals(expected: Tensor, actual: Tensor, epsilon: Double = 1e-9) {
        assertTrue(expected.shape.dimensions contentEquals actual.shape.dimensions, "Shape mismatch")
        assertEquals(expected.elements.size, actual.elements.size, "Size mismatch")
        for (i in expected.elements.indices) {
            assertTrue(Math.abs(expected.elements[i] - actual.elements[i]) < epsilon, "Value mismatch at index $i")
        }
    }

    @Test
    fun testEmbeddingForward() {
        val embedding = Embedding(10, 4) // 10 embeddings, each of dimension 4
        val input = Tensor(Shape(3), doubleArrayOf(0.0, 1.0, 3.0)) // Indices to retrieve embeddings for

        val output = embedding.forward(input)

        assertTrue(output.shape.dimensions contentEquals intArrayOf(3, 4), "Output shape should be (3, 4)")
    }

    @Test
    fun testEmbeddingIndexOutOfBounds() {
        val embedding = Embedding(5, 3) // 5 embeddings, each of dimension 3
        val input = Tensor(Shape(1), doubleArrayOf(5.0)) // Index out of bounds

        val exception = assertFailsWith<IllegalArgumentException> { embedding.forward(input) }
        assertEquals("Index out of bounds: 5", exception.message)

    }
}
