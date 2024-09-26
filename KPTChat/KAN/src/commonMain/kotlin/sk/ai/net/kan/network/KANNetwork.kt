package sk.ai.net.kan.network

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter
import sk.ai.net.kan.layer.KANLayer

class KANNetwork(
    val inputSize: Int,
    val hiddenSize: Int,
    val outputSize: Int,
    override val name: String = "KANNetwork"
) : Module() {

    private val layers = listOf(
        KANLayer(inputSize, hiddenSize, name = "HiddenLayer"),
        KANLayer(hiddenSize, outputSize, name = "OutputLayer")
    )

    override val params: List<NamedParameter> = layers.flatMap { it.params }
    override val modules: List<Module> = layers

    override fun forward(input: Tensor): Tensor {
        return layers.fold(input) { acc, layer -> layer.forward(acc) }
    }
}
