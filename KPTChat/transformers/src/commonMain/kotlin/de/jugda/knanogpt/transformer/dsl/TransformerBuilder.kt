package de.jugda.knanogpt.transformer.dsl

import de.jugda.knanogpt.transformer.Head
import org.skainet.dsl.*
import org.skainet.nn.Module

@DslMarker
annotation class TransformerDsl

@TransformerDsl
interface TransformerDslItem


@TransformerDsl
interface TransformerArchitectureDsl : NetworkDslItem {
    fun block(inputSize: Int, id: String = "")

    fun head(content: HEAD.() -> Unit = {})

    fun multihead(headsCount: Int = 6, content: MULTIHEAD.() -> Unit = {})

    fun feedForward(content: NeuralNetworkDsl.() -> Unit): Module
}

@TransformerDsl
interface HEAD : NetworkDslItem {
    var n_embd: Int
    var head_size: Int
    var dropout: Double

}

@TransformerDsl
interface MULTIHEAD : NetworkDslItem {
    var n_embd: Int
    var head_size: Int
    var dropout: Double

    fun head(content: HEAD.() -> Unit = {})
}


@TransformerDsl
fun transformer(content: TransformerArchitectureDsl.() -> Unit): List<Module> =
    TransformerArchitectureDslImpl().create()

@TransformerDsl
fun multiHead(headsCount: Int, content: MULTIHEAD.() -> Unit): List<Module> =
    MutliHeadImpl(headsCount).apply(content).create()

class HeadImpl() : HEAD {
    override var n_embd: Int = 0
    override var head_size: Int = 0
    override var dropout: Double = 0.0

    fun create(): HeadData = HeadData(n_embd, head_size, dropout)
}


@TransformerDsl
class MutliHeadImpl(private val headsCount: Int) : MULTIHEAD {
    private lateinit var head_data: HeadData
    override var n_embd: Int = 0
    override var head_size: Int = 0
    override var dropout: Double = 0.0

    var heads = mutableListOf<HEAD>()

    override fun head(content: HEAD.() -> Unit) {
        head_data = HeadImpl().apply(content).create()
    }

    fun create(): List<Module> = List(headsCount) { index ->
        Head(head_data.head_size, head_data.n_embd, head_data.dropout, index)
    }
}

data class HeadData(
    var n_embd: Int,
    var head_size: Int,
    var dropout: Double
)

class TransformerArchitectureDslImpl : TransformerArchitectureDsl {
    override fun block(inputSize: Int, id: String) {
        TODO("Not yet implemented")
    }

    override fun head(content: HEAD.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun multihead(headsCount: Int, content: MULTIHEAD.() -> Unit) {
        val impl = MutliHeadImpl(headsCount)
        impl.content()
    }


    override fun feedForward(content: NeuralNetworkDsl.() -> Unit): Module {
        TODO("Not yet implemented")
    }

    fun create(): List<Module> {
        TODO("Not yet implemented")
    }
}
