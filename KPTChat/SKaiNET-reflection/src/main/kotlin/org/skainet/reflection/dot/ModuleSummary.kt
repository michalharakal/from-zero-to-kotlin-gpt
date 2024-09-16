package org.skainet.reflection.dot

import org.markup.dsl.graphviz.dot.GraphViz
import org.markup.dsl.graphviz.dot.digraph
import org.skainet.nn.Linear
import org.skainet.nn.Module
import org.skainet.nn.by

fun Module.toDot(): GraphViz {

    return digraph {

        fun iterateModules(module: Module) {
            if (module.params.isNotEmpty()) {
                subgraph(module.name) {
                    label = module.name
                    if (module.modules.isNotEmpty()) {
                        module.modules.forEach { subModule ->
                            subgraph {
                                iterateModules(subModule)
                            }
                        }
                    } else {
                        if (module is Linear) {
                            val weight = module.params.by("W")!!.value.shape.dimensions[0]
                            repeat(weight) {
                                node("${module.name}-$it") {
                                    //label = module.name
                                }
                            }
                        }
                    }
                }
            }
        }

        this@toDot.modules.forEach { subModule ->
            iterateModules(subModule)
        }
    }

    /*
     for n in nodes:
    uid = str(id(n))
    # for any value in the graph, create a rectangular ('record') node for it
    dot.node(name=uid, label="{ %s | data %.4f | grad %.4f }" % (n.label, n.data, n.grad), shape='record')
    if n._op:
        # if this value is a result of some operation, create an op node for it
        dot.node(name=uid + n._op, label=n._op)
        # and connect this node to it
        dot.edge(uid + n._op, uid)

for n1, n2 in edges:
    # connect n1 to the op node of n2
    dot.edge(str(id(n1)), str(id(n2)) + n2._op)
     */
}
