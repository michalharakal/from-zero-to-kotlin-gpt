package org.skainet.init

import kotlin.math.sqrt
import kotlin.math.ln
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module


/**
 *  Kotlin does not have a built-in function to generate random numbers following a normal distribution directly,
 *  but we can use the Box-Muller transform to generate these numbers from uniformly distributed random numbers.
 *
 * The Box-Muller transform is a method for generating pairs of independent, standard,
 * normally distributed (mean = 0 and variance = 1) random numbers, given a pair of uniformly distributed random numbers.
 */
fun normalInit(tensor: Shape, mu: Double = 0.0, sigma: Double = 1.0, random: Random = Random.Default): Tensor {
    var z0: Double
    var z1 = 0.0
    var generate = false
    val elements = DoubleArray(tensor.volume)

    for (i in elements.indices) {
        if (!generate) {
            val u1 = random.nextDouble(from = 1e-100, until = 1.0)
            val u2 = random.nextDouble(from = 1e-100, until = 1.0)
            val mag = sigma * sqrt(-2.0 * ln(u1))
            z0 = mu + mag * cos(2.0 * Math.PI * u2)
            z1 = mu + mag * sin(2.0 * Math.PI * u2)
            elements[i] = z0
            generate = true
        } else {
            elements[i] = z1
            generate = false
        }
    }

    return Tensor(tensor, elements)
}

class InitNormal(
    nestedModule: Module,
    initHandler: (Tensor) -> Tensor,
    override val name: String
) : InitWrapperModule(nestedModule, initHandler, name)
