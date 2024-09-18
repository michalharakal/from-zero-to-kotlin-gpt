package de.jugda.knanogpt.core.tensor

fun Tensor.slice(from: Int, to: Int): Tensor =
    Tensor(Shape(this.shape.dimensions.size, to - from), elements.slice(from..to).toDoubleArray())

class TrainTestSplitter(private val data: Tensor) {
    fun split(factor: Float): Pair<Tensor, Tensor> {
        val n = (factor * data.shape.volume).toInt()
        val trainData = Tensor(Shape(n), data.elements.slice(0..n).toDoubleArray())
        val valData =
            Tensor(Shape(data.shape.volume - n), data.elements.slice(n..<data.shape.volume).toDoubleArray())
        return Pair(trainData, valData)
    }
}

fun stack(tensors: List<Tensor>, dim: Int = 0): Tensor {
    require(tensors.isNotEmpty()) { "Tensors list must not be empty." }
    require(tensors.map { it.shape.dimensions.toList() }.distinct().size == 1) {
        "All tensors must have the same shape."
    }


    val originalShape = tensors.first().shape
    require(dim in 0..originalShape.dimensions.size) {
        "Dimension out of range."
    }

    // Calculate the new shape
    val newShape = originalShape.dimensions.toMutableList().apply { add(dim, tensors.size) }.toIntArray()

    // Allocate new elements array
    val totalSize = newShape.reduce { acc, i -> acc * i }
    val newElements = DoubleArray(totalSize)

    // Fill newElements with tensor data
    var currentIndex = 0
    for (tensor in tensors) {
        tensor.elements.forEach {
            newElements[currentIndex++] = it
        }
    }

    return Tensor(Shape(*newShape), newElements)
}