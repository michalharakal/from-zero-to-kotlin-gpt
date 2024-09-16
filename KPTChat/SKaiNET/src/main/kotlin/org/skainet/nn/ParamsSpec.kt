package org.skainet.nn

import de.jugda.knanogpt.core.tensor.Tensor

data class NamedParameter(val name: String, var value: Tensor)

public fun List<NamedParameter>.by(name: String): NamedParameter? =
    firstOrNull { namedParameter -> namedParameter.name.uppercase().startsWith(name.uppercase()) }
