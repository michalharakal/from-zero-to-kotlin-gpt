package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import kotlin.math.pow
import kotlin.math.sqrt

// see https://arxiv.org/abs/1607.06450
class LayerNorm(
    private val normalizedSize: Int,
    override val name: String = "Embedding"
) : Module() {
    override val params: List<NamedParameter>
        get() = listOf(
            NamedParameter(
                "normalizedSize",
                Tensor(Shape(1), listOf(normalizedSize.toDouble()).toDoubleArray())
            )
        )


    override
    val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        // Assuming normalization across the last dimension
        val epsilon = 1e-7
        val lastDim = input.shape.dimensions.last()

        if (lastDim != normalizedSize) {
            throw IllegalArgumentException("Normalized size does not match the input tensor's last dimension size.")
        }

        val means = DoubleArray(input.shape.volume / lastDim) { 0.0 }
        val variances = DoubleArray(means.size) { 0.0 }

        for (i in input.elements.indices step lastDim) {
            var sum = 0.0
            for (j in 0 until lastDim) {
                sum += input.elements[i + j]
            }
            val mean = sum / lastDim
            means[i / lastDim] = mean

            var varianceSum = 0.0
            for (j in 0 until lastDim) {
                varianceSum += (input.elements[i + j] - mean).pow(2)
            }
            variances[i / lastDim] = varianceSum / lastDim
        }

        val normalizedElements = DoubleArray(input.elements.size)
        for (i in input.elements.indices step lastDim) {
            val mean = means[i / lastDim]
            val variance = variances[i / lastDim]
            for (j in 0 until lastDim) {
                val index = i + j
                normalizedElements[index] = (input.elements[index] - mean) / sqrt(variance + epsilon)
            }
        }

        return Tensor(input.shape, normalizedElements)
    }
}
