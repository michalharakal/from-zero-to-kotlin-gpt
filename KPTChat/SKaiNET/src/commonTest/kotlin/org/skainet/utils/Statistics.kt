package org.skainet.utils

fun calculateMean(elements: DoubleArray): Double {
    val sum = elements.sum()
    return sum / elements.size
}

fun calculateVariance(elements: DoubleArray, mean: Double): Double {
    val sumOfSquares = elements.sumOf { (it - mean) * (it - mean) }
    return sumOfSquares / elements.size
}