package sk.ai.net.kan.layer

import org.skainet.nn.Module
import org.skainet.nn.NamedParameter
import sk.ai.net.kan.spline.SplineFunction
import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.core.tensor.Shape
import kotlin.random.Random

class KANLayer(
    val inputSize: Int,
    val outputSize: Int,
    override val name: String = "KANLayer"
) : Module() {

    val splines: Array<Array<SplineFunction>> = Array(outputSize) {
        Array(inputSize) { SplineFunction(Random(42)) }
    }

    override val params: List<NamedParameter> = emptyList() // Parameters are embedded in the splines
    override val modules: List<Module> = emptyList()

    // Forward pass through the layer
    override fun forward(input: Tensor): Tensor {
        val outputElements = DoubleArray(outputSize)

        for (i in 0 until outputSize) {
            outputElements[i] = 0.0
            for (j in 0 until inputSize) {
                outputElements[i] += splines[i][j].apply(input.elements[j])
            }
        }

        return Tensor(Shape(outputSize), outputElements)
    }
}
