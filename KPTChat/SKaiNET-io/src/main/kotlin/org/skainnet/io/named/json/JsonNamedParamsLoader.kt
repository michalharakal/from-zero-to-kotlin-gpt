package org.skainnet.io.named.json

import de.jugda.knanogpt.core.tensor.Shape
import de.jugda.knanogpt.core.tensor.Tensor
import kotlinx.serialization.json.Json
import org.skainet.nn.NamedParameter
import org.skainnet.io.named.NamedParamsLoader
import java.io.File

class JsonNamedParamsLoader(private val jsonFile: File) : NamedParamsLoader {
    override fun load(namedParameterEvent: (NamedParameter) -> Unit) {
        // Example: Loading JSON from a file
        val jsonString = jsonFile.readText(Charsets.UTF_8)

        // Initialize Json object
        val json = Json { ignoreUnknownKeys = true }

        // Deserialize JSON to Kotlin objects
        val tensorItems: List<TensorItem> = json.decodeFromString(jsonString)

        // Emit an event for every item
        tensorItems.forEach { tensorItem ->
            namedParameterEvent(
                NamedParameter(
                    tensorItem.unique_parameter_name,
                    Tensor(
                        Shape(*tensorItem.tensor.shape.toIntArray()),
                        tensorItem.tensor.values.toDoubleArray()
                    )
                )
            )
        }
    }
}