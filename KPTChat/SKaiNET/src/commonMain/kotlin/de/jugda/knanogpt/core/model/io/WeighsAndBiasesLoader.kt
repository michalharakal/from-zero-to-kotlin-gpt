package de.jugda.knanogpt.core.model.io

import org.skainet.nn.NamedParameter


interface WeighsAndBiasesLoader {
    fun emit(event: (NamedParameter))
}
