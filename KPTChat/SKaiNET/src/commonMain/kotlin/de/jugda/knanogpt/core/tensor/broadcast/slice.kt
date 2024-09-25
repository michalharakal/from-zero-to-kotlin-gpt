package de.jugda.knanogpt.core.tensor.broadcast

import jp.co.qoncept.tensorkotlin.Shape
import jp.co.qoncept.tensorkotlin.Tensor
import kotlin.math.abs

data class Slice(val tensor: Tensor, val dimensionIndex: Int, val startIndex: Long, val endIndex: Long) {
    fun toRange() = startIndex..endIndex
}

fun Slice.start() = startIndex

fun Slice.end() = endIndex

fun Slice.all() = startIndex..tensor.shape.dimensions[dimensionIndex]

fun Slice.range(): LongRange = startIndex..endIndex

/**
slices {
// from second to the last
    slice {
        from 2 to last
    }
    // all elemnts, equals to ":" in pandas
    slice {
        all
    }
    // from 0 to the second last element
    slice {
        to -2
    }
}

slices {
    start {
        all()
    }
    end {
    }
}

 */

class SliceBuilder(private val tensor: Tensor, private val dimensionIndex: Int) {
    var startIndex: Long = 0
    var endIndex: Long = tensor.shape.dimensions[dimensionIndex].toLong()

    infix fun from(start: Int): FromBuilder {
        this.startIndex = start.toLong()
        return FromBuilder(this)
    }

    infix fun up(end: Int): FromBuilder {
        if (end > 0) {
            this.endIndex = end.toLong()
        } else {
            this.endIndex = tensor.shape.dimensions[dimensionIndex] - abs(end.toLong())
        }
        return FromBuilder(this)
    }

    fun all() {
        this.startIndex = 0
        this.endIndex = tensor.shape.dimensions[dimensionIndex].toLong()
    }

    fun none() {
        this.startIndex = -1
        this.endIndex = 0
    }


    fun build() = Slice(tensor, dimensionIndex, startIndex, endIndex)

    inner class FromBuilder(private val sliceBuilder: SliceBuilder) {
        infix fun to(end: Int) {
            sliceBuilder.endIndex = if (end == -1) tensor.shape.dimensions[dimensionIndex].toLong() else end.toLong()
        }
    }
}

class SlicesBuilder(private val tensor: Tensor) {
    private val slices = mutableListOf<Slice>()

    fun slice(init: SliceBuilder.() -> Unit) {
        val dimensionIndex = slices.size
        val builder = SliceBuilder(tensor, dimensionIndex)
        builder.init()
        slices.add(builder.build())
    }

    fun build() = slices
}

fun slices(tensor: Tensor, init: SlicesBuilder.() -> Unit): List<Slice> {
    val builder = SlicesBuilder(tensor)
    builder.init()
    return builder.build()
}

fun printVarargs(vararg elements: Slice) {
    for (element in elements) {
        println(element)
    }
}

fun main() {
    //val shape = Shape(3, 4, 5)
    //val tensor = Tensor(shape)
    val tensor = Tensor(Shape(3, 3), doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0))


    val sliceList = slices(tensor) {
        // from second to the last
        slice {
            from(1) to (-1)
        }
        // all elements, equals to ":"
        // from 0 to the second last element
        slice {
            up(-2)
        }
    }

    val s: Array<Slice> = sliceList.toTypedArray()
    printVarargs(*s)
    print(tensor.get(*s))

    //sliceList.forEach { println(tensor[it.toRange()]) }
}

