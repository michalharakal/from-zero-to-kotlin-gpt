package org.skainet.init

import de.jugda.knanogpt.core.tensor.Tensor
import jp.co.qoncept.tensorkotlin.Shape
import kotlin.random.Random


class GlorotNormal(gain: Double = 0.0, random: Random = Random.Default) : Initializer() {
    override fun fill(tensor: Tensor) {
        TODO("Not yet implemented")
    }

    override fun init(shape: Shape): Tensor {
        TODO("Not yet implemented")
    }

}

/**
 * Also known as Glorot initialization. http://proceedings.mlr.press/v9/glorot10a.html
 */
fun xavierUniform(tensor: Tensor, gain: Double = 0.0, random: Random = Random.Default) {

}

fun glorotNormal(tensor: Tensor, gain: Double = 0.0, random: Random = Random.Default) =
    xavierUniform(tensor, gain, random)