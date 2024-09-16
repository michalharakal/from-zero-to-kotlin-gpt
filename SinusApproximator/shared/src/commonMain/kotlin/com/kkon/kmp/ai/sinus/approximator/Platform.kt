package com.kkon.kmp.ai.sinus.approximator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform