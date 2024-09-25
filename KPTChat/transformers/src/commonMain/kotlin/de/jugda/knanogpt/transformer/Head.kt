package de.jugda.knanogpt.transformer

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Dropout
import org.skainet.nn.Module
import org.skainet.nn.Linear
import org.skainet.nn.NamedParameter
import de.jugda.knanogpt.core.tensor.ext.transpose
import kotlin.math.pow
import de.jugda.knanogpt.core.tensor.ext.maskedFill
import org.skainet.activations.softmax

class Head(
    n_embd: Int,
    head_size: Int,
    dropout: Double,
    val headNumber: Int
) : Module() {

    private val _modules = mutableListOf<Module>()

    init {
        _modules += listOf(
            Linear(n_embd, head_size, "key"),
            Linear(n_embd, head_size, "query"),
            Linear(n_embd, head_size, "value"),
            Dropout(dropout)
        )
    }

    override fun forward(input: Tensor): Tensor {
        val (B, T, C) = input.shape.dimensions
        val k = modules[0].forward(input) // key
        val q = modules[1].forward(input) // query
        val v = modules[2].forward(input) // value

        val wei = q.matmul(k.transpose(-2, -1)) * k.shape.dimensions.last().toDouble().pow(-0.5)
        val tril = Tensor(Shape(T, T), List(T * T) { 1.0 }.toDoubleArray())
        val ten =
            Tensor(tril.shape, tril.elements.mapIndexed { i, cc -> if (i % T >= i / T) cc else 0.0 }.toDoubleArray())
        val mask = tril.maskedFill(
            ten,
            Double.NEGATIVE_INFINITY
        )
        val weiMasked = wei + mask
        val weiSoftmax = weiMasked.softmax(-1)
        val out = weiSoftmax.matmul(v)
        return out
    }

    override val name: String
        get() = "Head-$headNumber"
    override val params: List<NamedParameter>
        get() = modules.map { it.params }.flatten()
    override val modules: List<Module>
        get() = _modules

}