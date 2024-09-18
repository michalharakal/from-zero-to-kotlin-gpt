package de.jugda.knanogpt.core.tensor


import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun stack() {
        val tensor1 = Tensor(Shape(3), listOf(1, 2, 3).map { it.toDouble() }.toDoubleArray())
        val tensor2 = Tensor(Shape(3), listOf(4, 5, 6).map { it.toDouble() }.toDoubleArray())
        val tensor3 = Tensor(Shape(3), listOf(7, 8, 9).map { it.toDouble() }.toDoubleArray())
        val a = stack(listOf(tensor1, tensor2, tensor3), 0)
        assertEquals(a.shape.volume, 9)
        assertEquals(a.shape.dimensions.size, 2)
        assertContentEquals(a.shape.dimensions, listOf(3, 3).toIntArray())
    }
}