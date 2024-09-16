package org.skainet.init

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter

open class InitWrapperModule(
    val module: Module,
    private val initHandler: (Tensor) -> Tensor,
    override val name: String
) : Module() {

    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()

    override fun forward(input: Tensor): Tensor {
        return initHandler(input)
    }
}