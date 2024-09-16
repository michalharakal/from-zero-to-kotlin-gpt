package org.skainet

import com.jakewharton.picnic.table
import de.jugda.knanogpt.core.tensor.Tensor
import de.jugda.knanogpt.core.tensor.ext.prod
import de.jugda.knanogpt.core.tensor.Shape
import org.skainet.nn.Module
import org.skainet.nn.by

data class NodeSummary(val name: String, val input: Shape, val output: Shape, val params: Long)

class Summary {

    val nodes = mutableListOf<NodeSummary>()

    private fun nodeSummary(index: Int, module: Module, input: Shape, output: Tensor): NodeSummary {
        var params = 0L

        module.params.by("W")?.let { weight ->
            val dimension = Tensor(Shape(1), weight.value.shape.dimensions.map { it.toDouble() }.toDoubleArray())
            params += dimension.prod().toLong()
        }

        module.params.by("B")?.let { weight ->
            val dimension = Tensor(Shape(1), weight.value.shape.dimensions.map { it.toDouble() }.toDoubleArray())
            params += dimension.prod().toLong()
        }


        return NodeSummary(
            "${module::class.simpleName}-$index",
            input,
            output.shape,
            params
        )
    }


    fun summary(model: Module, input: Shape, batch_size: Int = -1): List<NodeSummary> {
        var data = Tensor(input, List(input.volume) { 0.0 }.toDoubleArray())
        var count = 1
        model.modules.forEach { module ->
            val moduleInput = data
            data = module.forward(moduleInput)
            val nodeSummary = nodeSummary(count, module, moduleInput.shape, data)
            if (nodeSummary.params > 0) {
                count++
                nodes.add(nodeSummary)
            }
        }
        return nodes
    }

    fun printSummary(nodes: List<NodeSummary>) =
        table {
            cellStyle {
                border = true
            }
            header {
                row {
                    cell("Layer (type)")
                    //cell("Input Shape")
                    cell("Output Shape")
                    cell("Param #")
                }
            }
            nodes.forEach { node ->
                row {
                    cell(node.name)
                    //cell(node.input.toString())
                    cell(node.output.toString())
                    cell(node.params)
                }
            }
        }.toString()
}

fun Module.summary(input: Shape, batch_size: Int = -1): String {
    val summary = Summary()
    val nodes = summary.summary(this, input, batch_size)
    return summary.printSummary(nodes)
}
