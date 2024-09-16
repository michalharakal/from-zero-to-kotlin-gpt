package de.jugda.knanogpt.core.data

import kotlin.test.Test
import kotlin.test.assertEquals


class CharTokenizerTest {

    @Test
    fun `encode test`() {
        val provider = ResourcesDataProvider("input.txt")
        val tensor = provider.load()
        assertEquals(1, tensor.shape.dimensions.size)
    }
}