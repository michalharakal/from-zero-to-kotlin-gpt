package sk.ai.net.kan.spline

import kotlin.random.Random
import kotlin.math.pow


class SplineFunction(random: Random, val degree: Int = 3) {

    // Parameters for the spline (can be updated during training)
    val coefficients = DoubleArray(degree + 1) { random.nextDouble() }

    // Apply the spline function to an input value
    fun apply(x: Double): Double {
        var result = 0.0
        for (i in 0..degree) {
            result += coefficients[i] * x.pow(i.toDouble())
        }
        return result
    }

    // A method to update the coefficients (e.g., during gradient descent)
    fun update(gradients: DoubleArray, learningRate: Double) {
        for (i in 0..degree) {
            coefficients[i] -= learningRate * gradients[i]
        }
    }
}
