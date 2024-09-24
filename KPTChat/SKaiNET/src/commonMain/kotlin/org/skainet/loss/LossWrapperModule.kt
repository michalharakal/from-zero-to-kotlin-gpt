package org.skainet.loss

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter

open class LossWrapperModule(
    val module: Module,
    private val lossHandler: (Tensor, Tensor) -> Double,
    override val name: String
) : Module() {

    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        return input
    }

    fun loss(input: Tensor, labels: Tensor): Double {
        val output = module(input)
        return lossHandler(output, labels)
    }
}