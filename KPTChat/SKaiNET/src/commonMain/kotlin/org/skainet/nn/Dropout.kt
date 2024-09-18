package org.skainet.nn

import jp.co.qoncept.tensorkotlin.Tensor
import kotlin.random.Random


class Dropout(
    private val dropProbability: Double = 0.5,
    private val training: Boolean = false,
    private val random: Random = Random,
    override val name: String = "Dropout"
) : Module() {
    init {
        require(dropProbability in 0.0..1.0) { "Dropout probability has to be between 0 and 1, but got p = $dropProbability" }
    }

    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        // Check if the module is in training mode
        if (!training) {
            return input
        }

        // Create a mask with the same shape as the input tensor
        val mask = Tensor(input.shape, input.elements.map {
            if (random.nextDouble() > dropProbability) 1.0 else 0.0
        }.toDoubleArray())

        // Apply the mask to the input tensor
        return input * mask * (1.0 / (1.0 - dropProbability))
    }
}