package org.skainet.init

import de.jugda.knanogpt.core.tensor.Tensor
import jp.co.qoncept.tensorkotlin.Shape

abstract class Initializer {
    abstract fun fill(tensor: Tensor)
    abstract fun init(shape: Shape): Tensor
}