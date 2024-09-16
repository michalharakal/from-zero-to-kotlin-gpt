package org.skainet.activations

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter

/**
 * The ReLU function itself is simple: for a given input x, it returns x if x>0, and 0 otherwise.
 */
fun relu(x: Tensor): Tensor =
    Tensor(x.shape, x.elements.map { elem -> if (elem > 0) elem else 0.0 }.toDoubleArray())

class ReLU(override val name: String = "ReLU") : Module() {
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        return relu(input)
    }
}

val relu = ReLU()::forward

