package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Tensor


abstract class Module {

    abstract val name: String

    abstract val params: List<NamedParameter>

    abstract val modules: List<Module>

    abstract fun forward(input: Tensor): Tensor

    operator fun invoke(input: Tensor): Tensor {
        return forward(input)
    }
}