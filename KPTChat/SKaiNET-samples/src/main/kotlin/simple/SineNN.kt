package de.jugda.simple

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.summary
import org.skainet.activations.relu
import org.skainet.dsl.network
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter
import java.io.File

// https://www.hackster.io/news/easy-tinyml-on-esp32-and-arduino-a9dbc509f26c
import com.example.annotation.Builder
import org.skainnet.io.named.ModelsNamedParamsUpdater
import org.skainnet.io.named.json.JsonNamedParamsLoader
import java.nio.file.Files
import java.nio.file.Paths

@Builder
class SineNN(override val name: String = "SineNN") : Module() {

    private val sineModule = network {
        input(1)
        dense(16, "layer1") {
            activation = relu
        }
        dense(16, "layer2") {
            activation = relu
        }
        dense(1, "output_layer")
    }
    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = sineModule.modules

    override fun forward(input: Tensor): Tensor =
        sineModule.forward(input)
}


fun SineNN.of(value: Double): Tensor = this.forward(Tensor(Shape(1), listOf(value).toDoubleArray()))

fun SineNN.of(vararg values: Double): Tensor =
    this.forward(
        Tensor(
            Shape(values.size, 1), values
        )
    )


fun main() {
    val model = SineNN()
    println(model.summary(Shape(1), 1))
    val url = SineNN::class.java.classLoader.getResource("sinus_model_parameters.json")
    val uri = url?.toURI() ?: throw IllegalArgumentException("File not found in resources.")

    val path = Paths.get(uri)

    ModelsNamedParamsUpdater(
        JsonNamedParamsLoader(
            path.toFile()
        )
    ).update(model)
    print(model.of(kotlin.math.PI / 2))
}