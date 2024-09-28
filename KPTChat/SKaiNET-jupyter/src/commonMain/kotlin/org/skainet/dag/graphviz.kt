package org.skainet.dag

import com.github.yeicor.kraphviz.Kraphviz

/**
 * Converts graphviz dot with DAG into svg string
 */
fun dagDot2Svg(dotString: String): String {
    val kraphviz = Kraphviz()
    return kraphviz.render(dotString)
}