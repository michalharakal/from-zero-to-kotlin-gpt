package jp.co.qoncept.tensorkotlin

class Shape(vararg dimensions: Int) {
    val dimensions: IntArray = dimensions.copyOf()

    val volume: Int
        get() = dimensions.fold(1) { a, x -> a * x }

    override fun equals(other: Any?): Boolean {
        if (other !is Shape) {
            return false
        }

        return dimensions.size == other.dimensions.size && zipFold(dimensions, other.dimensions, true) { result, a, b ->
            if (!result) {
                return false
            }
            a == b
        }
    }

    override fun hashCode(): Int {
        return dimensions.hashCode()
    }

    override fun toString(): String {
        // Create a string representation of the dimensions array
        val dimensionsString = dimensions.joinToString(separator = " x ", prefix = "[", postfix = "]")
        // Return the formatted string including dimensions and volume
        return "Shape: Dimensions = $dimensionsString, Size (Volume) = $volume"
    }

}
