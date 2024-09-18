package org.skainet.topologies

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter

class FeedForwardNetwork(vararg modules: Module, override val name: String = "FeedForwardNetwork") : Module() {
    private val modulesList = modules.toList()
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = modulesList

    override fun forward(input: Tensor): Tensor {
        var tmp = input
        modulesList.forEach { module ->
            tmp = module.forward(tmp)
        }
        return tmp
    }
}