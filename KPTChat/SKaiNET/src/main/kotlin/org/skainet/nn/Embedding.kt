package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Shape
import jp.co.qoncept.tensorkotlin.Tensor
import kotlin.random.Random

class Embedding(
    private val numEmbeddings: Int,
    private val embeddingDim: Int,
    private val random: Random = Random.Default,
    override val name: String = "Embedding"
) : Module() {
    private val embeddings: Array<DoubleArray> =
        Array(numEmbeddings) { DoubleArray(embeddingDim) { random.nextDouble() - 0.5 } }
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        // Flatten the input tensor to work with indices
        val flatInput = input.elements.map { it.toInt() }
        val outputElements = DoubleArray(flatInput.size * embeddingDim)

        for ((index, value) in flatInput.withIndex()) {
            if (value !in 0 until numEmbeddings) throw IllegalArgumentException("Index out of bounds: $value")
            System.arraycopy(embeddings[value], 0, outputElements, index * embeddingDim, embeddingDim)
        }

        return Tensor(Shape(flatInput.size, embeddingDim), outputElements)
    }
}
