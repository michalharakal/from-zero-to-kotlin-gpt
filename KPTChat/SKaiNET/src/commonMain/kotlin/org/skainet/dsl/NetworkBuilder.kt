package org.skainet.dsl

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Input
import org.skainet.Dense
import org.skainet.activations.ActivationsWrapperModule
import org.skainet.topologies.FeedForwardNetwork
import org.skainet.nn.Module

// DSL Marker to restrict the DSL to its intended scope
@DslMarker
annotation class NetworkDsl

@NetworkDsl
fun network(content: NeuralNetworkDsl.() -> Unit) = NeuralNetworkDslImpl()
    .apply(content)
    .create()

@NetworkDsl
interface NetworkDslItem

@NetworkDsl
interface NeuralNetworkDsl : NetworkDslItem {
    fun input(inputSize: Int, id: String = "")

    fun dense(outputDimension: Int, id: String = "", content: DENSE.() -> Unit = {})

    fun embedding(numEmbeddings: Int, embeddingDim: Int, id: String = "")

    fun dropout(dropout: Double, id: String = "")
}

@NetworkDsl
interface DENSE : NetworkDslItem {
    var activation: (Tensor) -> Tensor
    fun weights(initBlock: (Shape) -> Tensor)
    fun bias(initBlock: (Shape) -> Tensor)
}

private fun getDefaultName(id: String, s: String, size: Int): String {
    if (id.isNotEmpty()) return id
    return "$s-$size"
}


class DenseImpl(
    private val inputDimension: Int, private val outputDimension: Int, private val id: String
) : DENSE {

    private var weightsValue: Tensor? = null
    private var _activation: (Tensor) -> Tensor = { tensor -> tensor }

    fun create(): List<Module> {
        return listOf(
            Dense(inputDimension, outputDimension, id),
            ActivationsWrapperModule(activation, "activation")
        )
    }

    override var activation: (Tensor) -> Tensor
        get() = _activation
        set(value) {
            _activation = value
        }

    override fun weights(initBlock: (Shape) -> Tensor) {
        weightsValue = initBlock(Shape(outputDimension, inputDimension))
    }

    override fun bias(initBlock: (Shape) -> Tensor) {
        weightsValue = initBlock(Shape(outputDimension))
    }
}

private class NeuralNetworkDslImpl : NeuralNetworkDsl {

    val modules = mutableListOf<Module>()
    var lastDimension = 0

    fun create() = NetworkBuilder().add(*modules.toTypedArray()).build()
    override fun input(inputSize: Int, id: String) {
        lastDimension = inputSize
        modules.add(Input(Shape(inputSize), name = getDefaultName(id, "Input", modules.size)))
    }

    override fun dense(outputDimension: Int, id: String, content: DENSE.() -> Unit) {
        val inputDimension = lastDimension
        lastDimension = outputDimension
        val impl = DenseImpl(
            inputDimension = inputDimension,
            outputDimension = outputDimension,
            id = getDefaultName(id, "linear", modules.size)
        )
        impl.content()
        // dense layer consinst from linear module and activation function module (2 modules)
        modules += impl.create()
    }

    override fun embedding(numEmbeddings: Int, embeddingDim: Int, id: String) {
        modules += org.skainet.nn.Embedding(
            numEmbeddings,
            embeddingDim,
            name = getDefaultName(id, "Embedding", modules.size)
        )
    }

    override fun dropout(dropout: Double, id: String) {
        modules.add(org.skainet.nn.Dropout(dropout, name = getDefaultName(id, "Dropout", modules.size)))
    }
}


@NetworkDsl
class NetworkBuilder {
    private val modules = mutableListOf<Module>()

    fun add(vararg modules: Module): NetworkBuilder {
        this.modules += modules.toList()
        return this
    }

    fun build(): Module = FeedForwardNetwork(*modules.toTypedArray())
}
