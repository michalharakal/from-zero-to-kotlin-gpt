package org.markup.dsl.graphviz.dot

// DSL Marker to restrict the DSL to its intended scope
@DslMarker
annotation class GraphvizDsl

@DslMarker
annotation class SubGraphvizDsl


@GraphvizDsl
fun graph(id: String = "root", content: GraphvizDotDsl.() -> Unit) = GraphvizDotDslImpl("graph", id)
    .apply(content)
    .create()

@GraphvizDsl
fun digraph(content: GraphvizDotDsl.() -> Unit) = GraphvizDotDslImpl("digraph")
    .apply {
        content()

    }.create()


@GraphvizDsl
interface GraphvizDotDslItem

fun generateId() = "id-${(0..100).random()}"

interface GraphvizDotDsl : GraphvizDotDslItem {

    fun subgraph(id: String = "", content: SubGraphvizDotDsl.() -> Unit = {})

    fun node(id: String = generateId(), content: NodeDsl.() -> Unit = {})

    fun edge(outputDimension: Int, id: String = "", content: EDGE.() -> Unit = {})

}

@SubGraphvizDsl
interface SubGraphvizDotDsl : GraphvizDotDslItem {

    var label: String

    fun subgraph(id: String = "", content: SubGraphvizDotDsl.() -> Unit = {})

    fun node(id: String = generateId(), content: NodeDsl.() -> Unit = {})

    fun edge(outputDimension: Int, id: String = "", content: EDGE.() -> Unit = {})

}


@GraphvizDsl
interface EDGE : GraphvizDotDslItem {

}

@GraphvizDsl
interface NodeDsl : GraphvizDotDslItem {
    var shape: String?
    var label: String?

}

class NodeDslImpl(private val id: String) : NodeDsl {

    private var _shape: String? = null
    override var shape: String?
        get() = _shape
        set(value) {
            _shape = value
        }

    private var _label: String? = null
    override var label: String?
        get() = _label
        set(value) {
            _label = value
        }


    fun create(): Node = Node(id, _label ?: "", _shape ?: "", null, null)
}

fun Subgraph.toDot(): String {
    return """
        |subgraph "$id" {
        | color=black;
		| node [style=solid,color=blue4, shape=circle];
		| label = "$id";
        |   ${subgraph.map { it.toDot() }.joinToString(";\n")}
        |   ${nodes.map { it.toDot() }.joinToString(";\n")}
        |   ${edges.map { it.toDot() }.joinToString(";\n")}
        |}
    """.trimMargin()
}

private fun Node.toDot(): String {
    return """
        |"$id" [label="$label", shape="${shape ?: ""}", color="${color ?: ""}", style="${style ?: ""}"]
    """.trimMargin()
}

private fun Edge.toDot(): String {
    return """
        |$from -> $to [label="$label", color="$color", style="$style"]
    """.trimMargin()
}

open class GraphvizDotDslImpl(
    private val graphType: String = "graph",
    private val graphId: String = generateId()
) : GraphvizDotDsl {

    private val subgraph = mutableListOf<Subgraph>()
    val nodes = mutableListOf<Node>()
    val edges = mutableListOf<Edge>()

    override fun subgraph(id: String, content: SubGraphvizDotDsl.() -> Unit) {
        val graph = SubgraphvizDotDslImpl("subgraph")
            .apply(content)
            .create()
        subgraph += Subgraph(id, graph.label, graph.subgraph, graph.nodes, graph.edges)
    }

    override fun node(id: String, content: NodeDsl.() -> Unit) {
        val nodeImpl = NodeDslImpl(id)
        nodeImpl.content()
        nodes += nodeImpl.create()
    }

    override fun edge(outputDimension: Int, id: String, content: EDGE.() -> Unit) {
        edges += Edge("dd", "dd", "dd", "", "")
    }

    fun create(): GraphViz = GraphViz(graphType, subgraph, nodes, edges)

}

class SubgraphvizDotDslImpl(
    private val subgraphId: String = generateId(),
) : SubGraphvizDotDsl {
    private val subgraph = mutableListOf<Subgraph>()
    val nodes = mutableListOf<Node>()
    val edges = mutableListOf<Edge>()

    private var _label = ""
    override var label: String
        get() {
            return _label
        }
        set(value) {
            _label = value
        }

    override fun subgraph(id: String, content: SubGraphvizDotDsl.() -> Unit) {
        val graph = SubgraphvizDotDslImpl(subgraphId)
        graph.content()

        subgraph += graph.create()
    }

    override fun node(id: String, content: NodeDsl.() -> Unit) {
        val nodeImpl = NodeDslImpl(id)
        nodeImpl.content()
        nodes += nodeImpl.create()
    }

    override fun edge(outputDimension: Int, id: String, content: EDGE.() -> Unit) {
        edges += Edge("dd", "dd", "dd", "", "")
    }

    fun create(): Subgraph = Subgraph(subgraphId, label, subgraph, nodes, edges)

}

data class Subgraph(
    val id: String,
    val label: String,
    val subgraph: List<Subgraph>,
    val nodes: List<Node>,
    val edges: List<Edge>
)

data class Node(
    val id: String,
    val label: String,
    val shape: String?,
    val color: String?,
    val style: String?
)

data class Edge(
    val from: String,
    val to: String,
    val label: String,
    val color: String,
    val style: String
)

data class GraphViz(
    val graphType: String,
    val subgraph: List<Subgraph>,
    val nodes: List<Node>,
    val edges: List<Edge>
)

fun GraphViz.render(): String {
    return """
            |digraph G {
            |   rankdir = LR
            |   splines = line
            |   ${subgraph.map { it.toDot() }.joinToString(";\n")}
            |   ${nodes.map { it.toDot() }.joinToString(";\n")}
            |   ${edges.map { it.toDot() }.joinToString(";\n")}
            |}
        """.trimMargin()
}
