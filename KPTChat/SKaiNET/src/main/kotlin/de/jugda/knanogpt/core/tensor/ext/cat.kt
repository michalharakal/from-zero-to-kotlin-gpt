package de.jugda.knanogpt.core.tensor.ext

import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.core.tensor.Shape

fun cat(tensors: List<Tensor>, dim: Int): Tensor {
    if (tensors.isEmpty()) throw IllegalArgumentException("Input tensor list cannot be empty")

    // Adjust the dimension if negative
    val actualDim = if (dim < 0) tensors[0].shape.dimensions.size + dim else dim

    // Ensure all tensors have the same shape except at actualDim
    val baseShape = tensors[0].shape.dimensions.copyOf()
    var totalSizeInDim = 0
    tensors.forEach { tensor ->
        if (tensor.shape.dimensions.size != baseShape.size) throw IllegalArgumentException("All tensors must have the same number of dimensions")
        for (i in tensor.shape.dimensions.indices) {
            if (i != actualDim && tensor.shape.dimensions[i] != baseShape[i]) {
                throw IllegalArgumentException("All tensors must have the same shape, except in the concatenating dimension")
            }
        }
        totalSizeInDim += tensor.shape.dimensions[actualDim]
    }

    // Compute the new shape
    val newShapeDims = baseShape.copyOf()
    newShapeDims[actualDim] = totalSizeInDim
    val newShape = Shape(*newShapeDims)

    // Concatenate the tensor elements
    val newElements = DoubleArray(newShape.volume)
    var offset = 0
    tensors.forEach { tensor ->
        System.arraycopy(tensor.elements, 0, newElements, offset, tensor.elements.size)
        offset += tensor.elements.size
    }

    return Tensor(newShape, newElements)
}
