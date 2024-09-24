package de.jugda.knanogpt.core.tensor.ext

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor

fun Tensor.t(): Tensor {
    // Ensure the tensor is 2D
    if (this.shape.dimensions.size != 2) {
        throw IllegalArgumentException("Transpose is only implemented for 2D tensors.")
    }

    // New shape with dimensions swapped
    val newShape = Shape(this.shape.dimensions[1], this.shape.dimensions[0])

    // Create a new elements array to hold the transposed elements
    val newElements = DoubleArray(this.elements.size)

    // Populate the new elements array with the transposed elements
    for (i in 0 until shape.dimensions[0]) { // Original rows
        for (j in 0 until shape.dimensions[1]) { // Original columns
            // Calculate the index in the original flat array and the new index in the transposed array
            val originalIndex = i * shape.dimensions[1] + j
            val newIndex = j * shape.dimensions[0] + i
            // Assign the transposed value
            newElements[newIndex] = this.elements[originalIndex]
        }
    }

    // Return a new tensor with the transposed shape and elements
    return Tensor(newShape, newElements)
}

fun Tensor.transpose(dim1: Int, dim2: Int): Tensor {
    if (shape.dimensions.size < 2) {
        return this
    }
    if (dim1 !in shape.dimensions.indices || dim2 !in shape.dimensions.indices) {
        throw IllegalArgumentException("Dimension indices are out of bounds.")
    }

    val newShape = Shape(*shape.dimensions.copyOf())
    newShape.dimensions[dim1] = shape.dimensions[dim2]
    newShape.dimensions[dim2] = shape.dimensions[dim1]

    val newElements = DoubleArray(shape.volume)
    val strides = computeStrides(shape.dimensions)

    for (index in elements.indices) {
        val newIndices = unravelIndex(index, shape.dimensions, strides)
        newIndices[dim1] = newIndices[dim2].also { newIndices[dim2] = newIndices[dim1] }

        val newIndex = ravelIndex(newIndices, newShape.dimensions, computeStrides(newShape.dimensions))
        newElements[newIndex] = elements[index]
    }

    return Tensor(newShape, newElements)
}

fun computeStrides(dimensions: IntArray): IntArray {
    val strides = IntArray(dimensions.size) { 1 }
    for (i in dimensions.lastIndex - 1 downTo 0) {
        strides[i] = strides[i + 1] * dimensions[i + 1]
    }
    return strides
}

fun unravelIndex(index: Int, dimensions: IntArray, strides: IntArray): IntArray {
    var idx = index
    val indices = IntArray(dimensions.size)
    for (i in strides.indices) {
        indices[i] = idx / strides[i]
        idx %= strides[i]
    }
    return indices
}

private fun ravelIndex(indices: IntArray, dimensions: IntArray, strides: IntArray): Int {
    var index = 0
    for (i in indices.indices) {
        index += indices[i] * strides[i]
    }
    return index
}

fun Tensor.prod(): Double = this.elements.fold(1.0) { acc, element -> acc * element }



