package org.skainet.diff.ops

import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.core.tensor.Shape

interface Operand {
    val value: Tensor
    var grad: Tensor
}

class GradientTensor(tensor: Tensor) : Operand {
    override val value = tensor
    override var grad = Tensor(tensor.shape, 0.0)
}

abstract class Operation(val inputs: List<Operand>) {

    abstract val output: Tensor
    abstract fun backward()

    fun resetGrads() {
        inputs.forEach { it.grad = Tensor(it.value.shape, 0.0) }
    }
}

class AddOperation(inputA: Tensor, inputB: Tensor) : Operation(listOf(GradientTensor(inputA), GradientTensor(inputB))) {
    override val output = inputA + inputB
    val gradientTensor = GradientTensor(output)

    override fun backward() {
        inputs[0].grad = gradientTensor.grad * 1.0
        inputs[1].grad = gradientTensor.grad * 1.0
    }
}

class MultiplyOperation(inputA: Tensor, inputB: Tensor) :
    Operation(listOf(GradientTensor(inputA), GradientTensor(inputB))) {
    override val output = inputA * inputB
    val gradientTensor = GradientTensor(output)

    override fun backward() {
        resetGrads()
        inputs[0].grad = inputs[1].value * gradientTensor.grad
        inputs[1].grad = inputs[0].value * gradientTensor.grad
    }
}

fun main() {
    val x = Tensor(Shape(1), 2.0)
    val y = Tensor(Shape(1), 3.0)
    val z = Tensor(Shape(1), 4.0)

    val sum = AddOperation(x, y)
    val result = MultiplyOperation(sum.output, z)

    println("Forward pass result: ${result.output}")

// Backward pass (assuming a loss gradient of 1 for demonstration)

    //result.output.grad = 1.0
    result.backward() // Backprop through multiply
    sum.backward() // Backprop through add

    //println("Gradient with respect to x: ${x.grad}")
    //println("Gradient with respect to y: ${y.grad}")
    //println("Gradient with respect to z: ${z.grad}")

}


