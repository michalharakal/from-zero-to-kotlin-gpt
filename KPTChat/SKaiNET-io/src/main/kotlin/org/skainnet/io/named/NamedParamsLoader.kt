package org.skainnet.io.named

import org.skainet.nn.NamedParameter


interface NamedParamsLoader {
    fun load(namedParameterEvent: (NamedParameter) -> Unit)
}
