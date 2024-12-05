package org.skainet.activations

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter

fun tanh(x: Tensor): Tensor =
    Tensor(x.shape, x.elements.map { elem -> if (elem > 0) elem else 0.0 }.toDoubleArray())

class Tanh(override val name: String = "Tanh") : Module() {
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        return tanh(input)
    }
}

val tanh = Tanh()::forward
