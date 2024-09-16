package de.jugda.de.jugda.knanogpt.llm


import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import org.skainet.nn.NamedParameter
import org.skainet.nn.Embedding


class BigramLanguageModel(
    vocabSize: Int,
    override val name: String = "Block"
) : Module() {
    private val tokenEmbeddingTable: Module = Embedding(vocabSize, vocabSize)

    override val params: List<NamedParameter>
        get() = emptyList()
    override val modules: List<Module>
        get() = listOf(tokenEmbeddingTable)

    override fun forward(input: Tensor): Tensor {

        //# idx and targets are both (B,T) tensor of integers
        return tokenEmbeddingTable(input) // # (B,T,C)
    }

    fun generate(input: Tensor, maxNewTokens: Int): Tensor {
        return input
        /*
        val idx = input
        val targets = input
        val B = idx.shape.dimensions[0]
        val T = idx.shape.dimensions[1]
        val C = idx.shape.dimensions[2]
        val newTokens = mutableListOf<Int>()
        for (i in 0 until maxNewTokens) {
            val logits = forward(idx)
            val nextToken = targets.elements[i].toInt()
            newTokens.add(nextToken)
        }
        return Tensor(Shape(*intArrayOf(B, maxNewTokens)), newTokens.map { it.toDouble() }.toDoubleArray())

         */
    }
}
