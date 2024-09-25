package de.jugda.knanogpt.transformer

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Dropout
import org.skainet.nn.Module
import org.skainet.nn.Linear
import org.skainet.nn.NamedParameter
import de.jugda.knanogpt.core.tensor.ext.cat
import de.jugda.knanogpt.transformer.dsl.transformer
import org.skainet.dsl.network


/**
 *     """ multiple heads of self-attention in parallel """
 *
 */
class MultiHeadAttention(
    config: TransformerConfig,
    override val name: String = "MultiHeadAttention"
) : Module() {

    private val _modules = mutableListOf<Module>()
    private val _heads = mutableListOf<Module>()

    init {
        with(config) {
            _heads += transformer {
                multihead(num_heads) {
                    head {
                        n_embd = config.n_embd
                        head_size = config.head_size
                        dropout = config.dropout
                    }

                }
            }
            _modules +=
                listOf(
                    network {
                        input(n_embd)
                        dense(head_size * num_heads)
                        dropout(dropout)
                    }
                )
        }
    }

    override val params: List<NamedParameter>
        get() = modules.map { it.params }.flatten()
    override val modules: List<Module>
        get() = _modules

    override fun forward(input: Tensor): Tensor {
        val conectcated = cat(
            _heads.map { head ->
                head.forward(input)
            },
            dim = -1
        )

        var data = conectcated
        _modules.forEach { module ->
            data = module.forward(data)
        }
        return data

    }
}


