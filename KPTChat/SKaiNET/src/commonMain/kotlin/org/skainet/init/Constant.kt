package org.skainet.init

import jp.co.qoncept.tensorkotlin.Shape
import jp.co.qoncept.tensorkotlin.Tensor

class Constant(private val value: Double) : Initializer() {
    override fun fill(tensor: Tensor) {
        TODO("Not yet implemented")
    }

    override fun init(shape: Shape): Tensor = Tensor(
        shape,
        List(shape.volume) { value }.map { it }.toDoubleArray()
    )
}