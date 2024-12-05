package org.skainet.init

import de.jugda.knanogpt.core.tensor.Tensor
import jp.co.qoncept.tensorkotlin.Shape

class Zeros : Initializer() {
    override fun fill(tensor: Tensor) {
        TODO("Not yet implemented")
    }

    override fun init(shape: Shape): Tensor = Tensor(
        shape,
        List(shape.volume) { 0.0 }.map { it }.toDoubleArray()
    )
}