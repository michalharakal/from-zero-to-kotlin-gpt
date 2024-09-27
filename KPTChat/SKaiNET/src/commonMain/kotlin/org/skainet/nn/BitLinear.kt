package org.skainet.nn


import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.core.tensor.ext.*

// Layer Normalization function
fun LN(x: Tensor, epsilon: Double = 1e-5): Tensor {
    val lastDim = x.shape.dimensions.size - 1
    val mean = x.mean(dim = lastDim, keepDim = true)
    val variance = x.variance(dim = lastDim, keepDim = true)

    // TODO fix this !!!
    //val normalized = (x - mean) / sqrt(epsilon + variance)

    return variance
}

// Activation quantization inference function
fun activationQuantInference(x: Tensor): Pair<Tensor, Tensor> {
    val xNorm = LN(x)
    val xAbs = xNorm.abs()
    val lastDim = x.shape.dimensions.size - 1
    val maxValues = xAbs.max(dim = lastDim, keepDim = true)
    // val scale = 127.0 / maxValues.clamp(min = 1e-5)
    //val y = (xNorm.times(scale.toDouble())).round().clamp(-128.0, 127.0)
    return Pair(x, x)
}


// Quantize weights to {-1, 0, 1}
fun quantizeWeights(w: Tensor): Tensor {
    val quantizedElements = w.elements.map {
        when {
            it > 0.5 -> 1.0
            it < -0.5 -> -1.0
            else -> 0.0
        }
    }.toDoubleArray()
    return Tensor(w.shape, quantizedElements)
}

// Compute weight scale
fun computeWeightScale(w: Tensor): Tensor {
    val maxAbs = w.abs().elements.maxOrNull() ?: 1.0
    return Tensor(Shape(1), doubleArrayOf(maxAbs))
}

// Efficient kernel for quantized matrix multiplication
fun efficientKernel(x: Tensor, w: Tensor): Tensor {
    return x.matmul(w.t())
}


/**
 * https://github.com/kyegomez/BitNet
 * The Era of 1-bit LLMs-2402.17764v1.pdf
 * https://arxiv.org/pdf/2402.17764
 *
 * @param inFeatures Number of input features
 * @param outFeatures Number of output features
 * @param name Name of the module
 * @param initWeights Initial weights
 * @param initBias Initial bias
 */
class BitLinear(val inputDim: Int, val outputDim: Int) : Module() {
    override val name: String = "BitLinear"

    val weight: Tensor
    val wScale: Tensor

    init {
        // Initialize weights and quantize them
        val weightInit =
            Tensor(Shape(outputDim, inputDim), listOf(kotlin.random.Random.nextDouble(-1.0, 1.0)).toDoubleArray())
        weight = quantizeWeights(weightInit)
        wScale = computeWeightScale(weightInit)
    }

    override val params: List<NamedParameter> = listOf(NamedParameter("weight", weight))
    override val modules: List<Module> = listOf()

    override fun forward(input: Tensor): Tensor {
        val (xQuant, xScale) = activationQuantInference(input)
        val y = efficientKernel(xQuant, weight) / (wScale * xScale)
        return y
    }
}
