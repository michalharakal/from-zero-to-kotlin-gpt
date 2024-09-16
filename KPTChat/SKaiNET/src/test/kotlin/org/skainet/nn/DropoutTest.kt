package org.skainet.nn


import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor

import kotlin.random.Random
import kotlin.test.Test

import kotlin.test.assertTrue

class SimpleDropoutTest {

    @Test
    fun testDropout() {
        val seed = 1234
        val dropout = Dropout(dropProbability = 0.5, training = true, Random(seed))
        val input = Tensor(Shape(1000), List(1000) { 1.0 }.toDoubleArray())
        val output = dropout.forward(input)

        val droppedOutCount = output.elements.count { it == 0.0 }
        val retainedCount = output.elements.size - droppedOutCount

        // Check if approximately 50% of the inputs were dropped
        assertTrue(droppedOutCount > 400, "Too few elements dropped")
        assertTrue(droppedOutCount < 600, "Too many elements dropped")

        // Alternatively, for a more precise test, calculate the proportion
        val dropProportion = droppedOutCount.toDouble() / output.elements.size
        assertTrue(dropProportion in 0.45..0.55, "Drop proportion not within expected range")
    }
}
