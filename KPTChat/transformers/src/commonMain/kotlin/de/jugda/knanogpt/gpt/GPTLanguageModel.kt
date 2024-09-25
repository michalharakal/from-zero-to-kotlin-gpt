package de.jugda.de.jugda.knanogpt.gpt

import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.transformer.TransformerConfig
import de.jugda.knanogpt.transformer.Block
import org.skainet.nn.*
import org.skainet.init.normalInit
import de.jugda.knanogpt.core.tensor.zeros

class GPTLanguageModel(config: TransformerConfig, override val name: String) : Module() {
    private val lm_head: Linear
    private val ln_f: LayerNorm
    private val blocks: List<Block>
    private val position_embedding_table: Embedding
    private val token_embedding_table: Embedding

    init {
        // each token directly reads off the logits for the next token from a lookup table
        with(config) {
            token_embedding_table = Embedding(vocab_size, n_embd)
            position_embedding_table = Embedding(block_size, n_embd)
            blocks = List(n_layer) {
                Block(config)
            }

            ln_f = LayerNorm(n_embd)
            lm_head = Linear(n_embd, vocab_size)
        }
        initWeights()
    }

    private fun initWeights() {
        modules.forEach { module ->
            if (module is Linear) {
                module.params.by("W")?.let { weights ->
                    weights.value = normalInit(weights.value.shape, 0.0, 0.0)
                }
                module.params.by("B")?.let { bias ->
                    bias.value = bias.value.shape.zeros()
                }
            }
            if (module is Embedding) {
                module.params.by("W")?.let { weights ->
                    weights.value = normalInit(weights.value.shape, 0.0, 0.0)
                }
            }
        }
    }


    override val params: List<NamedParameter>
        get() = modules.map { module -> module.params }.flatten()
    override val modules: List<Module>
        get() = listOf(token_embedding_table, position_embedding_table, ln_f, lm_head) + blocks

    override fun forward(input: Tensor): Tensor {
        return token_embedding_table(input) // # (B,T,C)
            .let { it + position_embedding_table(it) } // # (B,T,C)
            .let { blocks.fold(it) { acc, block -> block(acc) } } // # (B,T,C)
            .let { ln_f(it) } // # (B,T,C)
            .let { lm_head(it) } // # (B,T,V)
    }
}