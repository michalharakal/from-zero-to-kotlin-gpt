package org.skainet.test

import kotlin.math.abs
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Asserts that two arrays are equal. If they are not, an assertion error is thrown.
 *
 * @param expected the expected array
 * @param actual the actual array
 * @param message the error message (optional)
 */
fun <T> assertArrayEquals(expected: Array<T>, actual: Array<T>, message: String? = null) {
    if (!expected.contentEquals(actual)) {
        fail(
            message
                ?: "Expected arrays to be equal. Expected: ${expected.contentToString()}, but was: ${actual.contentToString()}"
        )
    }
}

/**
 * Asserts that two primitive int arrays are equal. If they are not, an assertion error is thrown.
 *
 * @param expected the expected array
 * @param actual the actual array
 * @param message the error message (optional)
 */
fun assertArrayEquals(expected: IntArray, actual: IntArray, message: String? = null) {
    if (!expected.contentEquals(actual)) {
        fail(
            message
                ?: "Expected int arrays to be equal. Expected: ${expected.contentToString()}, but was: ${actual.contentToString()}"
        )
    }
}

// Similarly, we can implement for other primitive arrays (ByteArray, DoubleArray, etc.)
fun assertArrayEquals(expected: DoubleArray, actual: DoubleArray, message: String? = null) {
    if (!expected.contentEquals(actual)) {
        fail(
            message
                ?: "Expected double arrays to be equal. Expected: ${expected.contentToString()}, but was: ${actual.contentToString()}"
        )
    }
}

fun assertArrayEquals(expected: FloatArray, actual: FloatArray, message: String? = null) {
    if (!expected.contentEquals(actual)) {
        fail(
            message
                ?: "Expected float arrays to be equal. Expected: ${expected.contentToString()}, but was: ${actual.contentToString()}"
        )
    }
}

/**
 * Asserts that two double arrays are equal within a delta tolerance.
 *
 * @param expected the expected array
 * @param actual the actual array
 * @param delta the acceptable difference between two double values
 * @param message the error message (optional)
 */
fun assertArrayEquals(expected: DoubleArray, actual: DoubleArray, delta: Double, message: String? = null) {
    if (expected.size != actual.size) {
        fail(message ?: "Arrays are of different lengths. Expected: ${expected.size}, but was: ${actual.size}")
    }

    for (i in expected.indices) {
        if (abs(expected[i] - actual[i]) > delta) {
            fail(
                message
                    ?: "Arrays differ at index $i. Expected: ${expected.contentToString()}, but was: ${actual.contentToString()}"
            )
        }
    }
}

// You can add similar functions for LongArray, ShortArray, etc.
