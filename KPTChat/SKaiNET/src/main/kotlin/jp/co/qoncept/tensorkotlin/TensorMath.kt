package jp.co.qoncept.tensorkotlin

import kotlin.math.*

fun Tensor.pow(tensor: Tensor): Tensor {
    assert(
        { shape == tensor.shape },
        { "Incompatible shapes of tensors: this.shape = ${shape}, tensor.shape = ${tensor.shape}" })
    return Tensor(shape, zipMap(elements, tensor.elements) { a, b -> a.pow(b) })
}

fun Tensor.pow(scalar: Double): Tensor {
    return Tensor(shape, elements.map { it.pow(scalar) }.toDoubleArray())
}

val Tensor.sin: Tensor
    get() = Tensor(shape, elements.map { sin(it) }.toDoubleArray())

val Tensor.cos: Tensor
    get() = Tensor(shape, elements.map { cos(it) }.toDoubleArray())

val Tensor.tan: Tensor
    get() = Tensor(shape, elements.map { tan(it) }.toDoubleArray())

val Tensor.asin: Tensor
    get() = Tensor(shape, elements.map { asin(it) }.toDoubleArray())

val Tensor.acos: Tensor
    get() = Tensor(shape, elements.map { acos(it) }.toDoubleArray())

val Tensor.atan: Tensor
    get() = Tensor(shape, elements.map { atan(it) }.toDoubleArray())

val Tensor.sinh: Tensor
    get() = Tensor(shape, elements.map { sinh(it) }.toDoubleArray())

val Tensor.cosh: Tensor
    get() = Tensor(shape, elements.map { cosh(it) }.toDoubleArray())

val Tensor.tanh: Tensor
    get() = Tensor(shape, elements.map { tanh(it) }.toDoubleArray())

val Tensor.exp: Tensor
    get() = Tensor(shape, elements.map { exp(it) }.toDoubleArray())

val Tensor.log: Tensor
    get() = Tensor(shape, elements.map { ln(it) }.toDoubleArray())

val Tensor.sqrt: Tensor
    get() = Tensor(shape, elements.map { sqrt(it) }.toDoubleArray())

val Tensor.cbrt: Tensor
    get() = Tensor(shape, elements.map { cbrt(it) }.toDoubleArray())

val Tensor.sigmoid: Tensor
    get() = Tensor(shape, elements.map { (1.0 / exp(-it)) }.toDoubleArray())


val Tensor.ln: Tensor
    get() = Tensor(shape, elements.map { ln(it) }.toDoubleArray())
