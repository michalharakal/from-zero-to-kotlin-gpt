package de.jugda.knanogpt.core.tensor.broadcast

import de.jugda.knanogpt.core.tensor.*
import kotlin.math.max

import de.jugda.knanogpt.core.tensor.ext.plus

private val Tensor.indices: Strides
    get() = RowStrides(shape)


internal fun broadcastShapes(shapes: List<Shape>): Shape {
    var totalDim = 0
    for (shape in shapes) {
        totalDim = max(totalDim, shape.dimensions.size)
    }

    val totalShape = IntArray(totalDim) { 0 }
    for (shape in shapes) {
        for (i in shape.dimensions.indices) {
            val curDim = shape.dimensions[i]
            val offset = totalDim - shape.dimensions.size
            totalShape[i + offset] = max(totalShape[i + offset], curDim)
        }
    }

    for (shape in shapes) {
        for (i in shape.dimensions.indices) {
            val curDim = shape.dimensions[i]
            val offset = totalDim - shape.dimensions.size
            check(curDim == 1 || totalShape[i + offset] == curDim) {
                "Shapes are not compatible and cannot be broadcast"
            }
        }
    }

    return Shape(*totalShape)
}


fun broadcastTo(tensor: Tensor, newShape: Shape): Tensor {
    val tensors = List(newShape.dimensions[0]) {
        tensor
    }
    return stack(tensors)
}

internal fun broadcastOuterTensors(vararg tensors: Tensor): List<Tensor> {
    val onlyTwoDims = tensors.asSequence().onEach {
        require(it.shape.dimensions.size >= 2) {
            "Tensors must have at least 2 dimensions"
        }
    }.any { it.shape.dimensions.size != 2 }

    if (!onlyTwoDims) {
        return tensors.asList()
    }

    val sliced: List<Shape> = tensors.map { tensor ->
        tensor.shape.dimensions.slice(0..tensor.shape.dimensions.size - 3)
    }.map { Shape(*it.toIntArray()) }


    val totalShape = broadcastShapes(sliced)
    val n = totalShape.volume

    return buildList {
        for (tensor in tensors) {
            val matrixShape = Shape(
                *tensor.shape.dimensions.slice(tensor.shape.dimensions.size - 2 until tensor.shape.dimensions.size)
                    .toIntArray()
            )
            val matrixSize = matrixShape.dimensions[0] * matrixShape.dimensions[1]
            val matrix = Tensor(matrixShape, DoubleArray(matrixSize))

            val outerTensor = Tensor(totalShape, DoubleArray(n))
            val resTensor = Tensor(totalShape + matrixShape, DoubleArray(n * matrixSize))

            for (linearIndex in 0 until n) {
                val totalMultiIndex = outerTensor.indices.index(linearIndex)

                var curMultiIndex = tensor.shape.dimensions.slice(0..tensor.shape.dimensions.size - 3).toIntArray()
                curMultiIndex = IntArray(totalMultiIndex.size - curMultiIndex.size) { 1 } + curMultiIndex

                val newTensor = Tensor(Shape(*curMultiIndex) + matrixShape, tensor.elements)

                for (i in curMultiIndex.indices) {
                    if (curMultiIndex[i] != 1) {
                        curMultiIndex[i] = totalMultiIndex[i]
                    } else {
                        curMultiIndex[i] = 0
                    }
                }

                for (i in 0 until matrixSize) {
                    val curLinearIndex = newTensor.indices.offset(
                        curMultiIndex +
                                matrix.indices.index(i)
                    )
                    val newLinearIndex = resTensor.indices.offset(
                        totalMultiIndex +
                                matrix.indices.index(i)
                    )

                    resTensor.elements[newLinearIndex] =
                        newTensor.elements[curLinearIndex]
                }
            }
            add(resTensor)
        }
    }
}
