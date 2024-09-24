package com.kkon.kmp.ai.sinus.approximator

import de.jugda.knanogpt.core.tensor.Tensor
import jp.co.qoncept.tensorkotlin.Shape
import org.skainet.activations.relu
import org.skainet.dsl.network
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter

interface SinusCalculator {
    fun calculate(x: Double): Double
    fun loadModel()
}


class SineNN(override val name: String = "sin") : Module() {

    private val sineModule = network {
        input(1)
        dense(16) {
            activation = relu
        }
        dense(16) {
            activation = relu
        }
        dense(1)
    }
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = sineModule.modules

    override fun forward(input: Tensor): Tensor =
        sineModule.forward(input)
}


public fun SineNN.of(angle: Double): Tensor = this.forward(Tensor(Shape(1), listOf(angle.toDouble()).toDoubleArray()))