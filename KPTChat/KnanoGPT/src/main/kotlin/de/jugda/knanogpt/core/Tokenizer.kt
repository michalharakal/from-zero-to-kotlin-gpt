package de.jugda.knanogpt.core

interface Tokenizer<T> {

    fun encode(text: String): List<T>

    fun decode(tokens: List<T>): String
}