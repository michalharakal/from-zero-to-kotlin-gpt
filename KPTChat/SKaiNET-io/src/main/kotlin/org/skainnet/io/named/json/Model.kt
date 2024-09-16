package org.skainnet.io.named.json

import kotlinx.serialization.Serializable

@Serializable
data class TensorItem(
    val unique_parameter_name: String,
    val tensor: Tensor
)

@Serializable
data class Tensor(
    val shape: List<Int>,
    val values: List<Double>
)