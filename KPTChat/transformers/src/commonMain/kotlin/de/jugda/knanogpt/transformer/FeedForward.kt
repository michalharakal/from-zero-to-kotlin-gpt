package de.jugda.knanogpt.transformer

import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.transformer.TransformerConfig
import org.skainet.activations.relu
import org.skainet.dsl.network
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter
import org.skainet.topologies.FeedForwardNetwork


class FeedForward(
    config: TransformerConfig,
    override val name: String = "FeedForward"
) : Module() {

    private val sequential: Module

    init {
        with(config) {
            sequential = network {
                input(n_embd)
                dense(4 * n_embd) {
                    activation = relu

                }
                dense(n_embd)
                dropout(dropout)
            }
        }
    }

    override val params: List<NamedParameter>
        get() = sequential.params
    override val modules: List<Module>
        get() = sequential.modules

    override fun forward(input: Tensor): Tensor {
        return sequential.forward(input)
    }
}
