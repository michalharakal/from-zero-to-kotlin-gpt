package org.skainet.dsl

import org.skainet.nn.Module

@NetworkDsl
fun train(content: TrainNeuralNetworkDsl.() -> Unit) = TrainNeuralNetworkDslImpl()
    .apply(content)
    .create()

@NetworkDsl
interface TrainNeuralNetworkDsl : NetworkDslItem {
}


class TrainNeuralNetworkDslImpl : TrainNeuralNetworkDsl {
    fun create(): List<Module> = emptyList()
}


