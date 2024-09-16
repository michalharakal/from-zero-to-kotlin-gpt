package de.jugda.knanogpt.core.tensor

import kotlin.random.Random


/**
 * Returns a tensor filled with random integers generated uniformly between low (inclusive) and high (exclusive).
 * The shape of the tensor is defined by the variable argument size.
 */
fun randint(random: Random, low: Int, high: Int, shape: Shape): Tensor {
    require(high > low) { "High bound must be greater than low bound." }
    val elements = DoubleArray(shape.volume) { random.nextInt(low, high).toDouble() }
    return Tensor(shape, elements)
}

fun Shape.zeros(): Tensor = Tensor(this, DoubleArray(volume) { 0.0 })

fun Shape.ones(): Tensor = Tensor(this, DoubleArray(volume) { 1.0 })