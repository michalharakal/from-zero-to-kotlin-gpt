package sk.ai.net.kan

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import sk.ai.net.kan.network.KANNetwork
import sk.ai.net.kan.train.trainKAN

fun main() {
    val inputSize = 1
    val hiddenSize = 10
    val outputSize = 1
    val network = KANNetwork(inputSize, hiddenSize, outputSize)

    trainKAN(network)

    // Test the network with some values
    val testInput = 1.0 // Example input
    val inputTensor = Tensor(Shape(1), doubleArrayOf(testInput))
    val output = network.forward(inputTensor)

    println("Test input: $testInput, Network output: ${output.elements[0]}, Expected: ${kotlin.math.sin(testInput)}")
}
