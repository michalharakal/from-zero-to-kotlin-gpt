package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.core.tensor.ext.t

/**
 * Linear layer (a.k.a. fully connected dense layer). This layer applies a linear transformation to the input data.
 * The weights and biases are learned during training.
 *
 * @param inFeatures Number of input features
 * @param outFeatures Number of output features
 * @param name Name of the module
 * @param initWeights Initial weights
 * @param initBias Initial bias
 */

class Linear(
    inFeatures: Int,
    outFeatures: Int,
    override val name: String = "Linear",
    initWeights: Tensor = Tensor(
        Shape(outFeatures, inFeatures),
        List(inFeatures * outFeatures) { 0.0 }.map { it }.toDoubleArray()
    ),
    initBias: Tensor = Tensor(
        Shape(outFeatures),
        List(inFeatures * outFeatures) { 0.0 }.map { it }.toDoubleArray()
    )
) : Module() {
    private val bias = NamedParameter("bias", initBias)
    private val weight = NamedParameter("weight", initWeights)

    override val params: List<NamedParameter>
        get() = listOf(weight, bias)


    override val modules: List<Module>
        get() = emptyList()


    override fun forward(input: Tensor): Tensor {
        val weight = params.by("W")!!
        val bias = params.by("B")!!

        // Assuming Tensor has a matmul (matrix multiplication) method and a plus method for addition
        val output = input.matmul(weight.value.t()) + bias.value
        return output
    }

    fun backward(input: Tensor, gradOutput: Tensor) {
        val weight = params.by("W")!!
        val bias = params.by("B")!!

        // Conceptual gradient computation for weights and biases
        weight.value = input.t().matmul(gradOutput) // Simplified; real implementation needs proper reshaping
        bias.value = gradOutput//.sum(0) // Assuming gradOutput's shape is compatible
    }
}