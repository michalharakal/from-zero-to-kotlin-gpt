package com.kkon.kmp.ai.sinus.approximator

interface SinusCalculator {
    fun calculate(x: Double): Double
    fun loadModel()
}

expect fun getSinusCalculator(): SinusCalculator