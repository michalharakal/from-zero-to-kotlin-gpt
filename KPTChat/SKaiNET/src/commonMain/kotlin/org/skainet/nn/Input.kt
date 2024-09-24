package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Shape
import jp.co.qoncept.tensorkotlin.Tensor

class Input(private val inputShape: Shape, override val name: String = "Input") : Module() {
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = emptyList()


    override fun forward(input: Tensor): Tensor {
        return input
    }
}