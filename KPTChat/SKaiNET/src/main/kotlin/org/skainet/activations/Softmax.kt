package org.skainet.activations

import org.skainet.nn.Module
import org.skainet.nn.NamedParameter
import de.jugda.knanogpt.core.tensor.Tensor
import jp.co.qoncept.tensorkotlin.exp

fun Tensor.softmax(): Tensor {
    val exps = exp
    val sum = exps.elements.fold(0.0) { r, x -> r + x }
    return exps / sum
}

fun Tensor.softmax(dim: Int): Tensor {
    val actualDim = if (dim < 0) shape.dimensions.size + dim else dim
    if (actualDim < 0 || actualDim >= shape.dimensions.size) {
        throw IllegalArgumentException("Dimension out of range")
    }

    // Compute the exponential of each element and the sum of exponentials along the specified dimension.
    val exps = DoubleArray(elements.size)
    val sumExps = DoubleArray(shape.volume / shape.dimensions[actualDim]) { 0.0 }

    val strides = de.jugda.knanogpt.core.tensor.ext.computeStrides(shape.dimensions)
    for (index in elements.indices) {
        val indices = de.jugda.knanogpt.core.tensor.ext.unravelIndex(index, shape.dimensions, strides)
        val dimIndex = indices[actualDim]
        val exp = Math.exp(elements[index])
        exps[index] = exp
        sumExps[dimIndex] += exp
    }

    // Normalize by the sum of exponentials to get softmax probabilities.
    val softmaxElements = DoubleArray(elements.size)
    for (index in elements.indices) {
        val indices = de.jugda.knanogpt.core.tensor.ext.unravelIndex(index, shape.dimensions, strides)
        val dimIndex = indices[actualDim]
        softmaxElements[index] = exps[index] / sumExps[dimIndex]
    }

    return Tensor(shape, softmaxElements)
}


class Softmax(private val dimension: Int, override val name: String = "Softmax") : Module() {
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        return input.softmax(dimension)
    }
}

