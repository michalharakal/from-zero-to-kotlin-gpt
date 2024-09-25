package de.jugda.knanogpt.transformer.dsl

import org.skainet.dsl.NetworkDsl
import org.skainet.dsl.NetworkDslItem
import org.skainet.dsl.NeuralNetworkDsl
import org.skainet.nn.Module


@NetworkDsl
interface TransformerArchitectureDsl : NetworkDslItem {
    fun block(inputSize: Int, id: String = "")

    fun head(content: HEAD.() -> Unit = {})

    fun multihead(headsCount: Int = 6, content: MULTIHEAD.() -> Unit = {})

    fun feedForward(content: NeuralNetworkDsl.() -> Unit): Module
}

@NetworkDsl
interface HEAD : NetworkDslItem {
    var n_embd: Int
    var head_size: Int
    var dropout: Double

}

@NetworkDsl
interface MULTIHEAD : NetworkDslItem {
    var n_embd: Int
    var head_size: Int
    var dropout: Double

    fun head(content: HEAD.() -> Unit = {})
}


fun transformer(content: TransformerArchitectureDsl.() -> Unit): List<Module> =
    TransformerArchitectureDslImpl().create()

class TransformerArchitectureDslImpl : TransformerArchitectureDsl {
    override fun block(inputSize: Int, id: String) {
        TODO("Not yet implemented")
    }

    override fun head(content: HEAD.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun multihead(headsCount: Int, content: MULTIHEAD.() -> Unit) {
        TODO("Not yet implemented")
    }


    override fun feedForward(content: NeuralNetworkDsl.() -> Unit): Module {
        TODO("Not yet implemented")
    }

    fun create(): List<Module> {
        TODO("Not yet implemented")
    }
}
