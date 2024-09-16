package de.jugda.knanogpt.core.tensor.ext

import de.jugda.knanogpt.core.tensor.Tensor


fun Tensor.maskedFill(mask: Tensor, fillValue: Double): Tensor {
    if (!shape.dimensions.contentEquals(mask.shape.dimensions)) {
        throw IllegalArgumentException("Shape of mask tensor must match shape of the original tensor.")
    }

    val newElements = elements.copyOf()
    for (i in elements.indices) {
        if (mask.elements[i] == 1.0) {
            newElements[i] = fillValue
        }
    }

    return Tensor(shape, newElements)
}

fun Tensor.tril(): Tensor {
    // Scalars and vectors are returned as is
    if (shape.dimensions.size < 2) {
        return this
    }

    // For matrices and higher, apply the tril operation only on the last two dimensions
    val rows = shape.dimensions[shape.dimensions.size - 2]
    val cols = shape.dimensions[shape.dimensions.size - 1]
    val newElements = elements.copyOf()

    for (i in 0 until rows) {
        for (j in i + 1 until cols) {
            newElements[i * cols + j] = 0.0
        }
    }

    return Tensor(shape, newElements)
}