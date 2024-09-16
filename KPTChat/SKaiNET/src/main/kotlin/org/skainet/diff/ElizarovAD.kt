package org.skainet.diff

import de.jugda.knanogpt.core.tensor.Tensor
import jp.co.qoncept.tensorkotlin.*


/*
 * Implementation of backward-mode automatic differentiation.
 *
 * @author Roman Elizarov
 *
 * https://gist.github.com/elizarov/1ad3a8583e88cb6ea7a0ad09bb591d3d
 */

/**
 * Differentiable variable with value and derivative of differentiation ([grad]) result
 * with respect to this variable.
 */
data class D(var x: Tensor, var d: Tensor = Tensor(x.shape, List(x.elements.size) { 0.0 }.toDoubleArray()))

/**
 * Runs differentiation and establishes [AD] context inside the block of code.
 *
 * Example:
 * ```
 * val x = D(2) // define variable(s) and their values
 * val y = grad { sqr(x) + 5 * x + 3 } // write formulate in grad context
 * assertEquals(17.0, y.x) // the value of result (y)
 * assertEquals(9.0, x.d)  // dy/dx
 * ```
 */
fun grad(body: AD.() -> D): D =
    ADImpl().run {
        val result = body()
        result.d = Tensor(
            result.x.shape,
            List(result.x.elements.size) { 1.0 }.toDoubleArray()
        ) // computing derivative w.r.t result
        runBackwardPass()
        result
    }

/**
 * Automatic Differentiation context class.
 */
abstract class AD {
    /**
     * Performs update of derivative after the rest of the formula in the back-pass.
     *
     * For example, implementation of `sin` function is:
     *
     * ```
     * fun AD.sin(x: D): D = derive(D(sin(x.x)) { z -> // call derive with function result
     *     x.d += z.d * cos(x.x) // update derivative using chain rule and derivative of the function
     * }
     * ```
     */
    abstract fun <R> derive(value: R, block: (R) -> Unit): R

    // Basic math (+, -, *, /)

    operator fun D.plus(that: D): D =
        derive(D(this.x + that.x, Tensor(this.x.shape, List(this.x.elements.size) { 1.0 }.toDoubleArray()))) { z ->
            this.d += z.d
            that.d += z.d
        }

    operator fun D.minus(that: D): D = derive(D(this.x - that.x)) { z ->
        this.d += z.d
        that.d -= z.d
    }

    operator fun Double.times(that: D): D = derive(D(that.x * this)) { z ->
        that.d += z.d * this
    }

    operator fun D.times(b: Double): D = b.times(this)


    operator fun D.times(that: D): D = derive(D(this.x * that.x)) { z: D ->

        this.d += that.x * z.d
        that.d += z.d * this.x
    }

    operator fun D.div(that: D): D = derive(D(this.x / that.x)) { z ->
        this.d += z.d / that.x
        that.d -= z.d * this.x / (that.x * that.x)
    }


    // Overloads for Double constants

    operator fun D.minus(that: Double): D = derive(D(this.x - that)) { z ->
        this.d += z.d
    }


    /*
    operator fun Double.div(that: D): D = derive(D(this / that.x)) { z ->
        that.d -= z.d * this / (that.x * that.x)
    }
    */

    operator fun D.div(that: Double): D = derive(D(this.x / that)) { z ->
        this.d += z.d / that
    }


    // Overloads for Int constants
    /*
    operator fun Int.plus(b: D): D = toDouble().plus(b)
    operator fun D.plus(b: Int): D = plus(b.toDouble())
    operator fun Int.minus(b: D): D = toDouble().minus(b)
    operator fun D.minus(b: Int): D = minus(b.toDouble())
    operator fun Int.times(b: D): D = toDouble().times(b)
    operator fun D.times(b: Int): D = times(b.toDouble())
    operator fun Int.div(b: D): D = toDouble().div(b)
    operator fun D.div(b: Int): D = div(b.toDouble())

     */
}

// ---------------------------------------- ENGINE IMPLEMENTATION ----------------------------------------

// Private implementation class
private class ADImpl : AD() {
    // this stack contains pairs of blocks and values to apply them to
    private var stack = arrayOfNulls<Any?>(8)
    private var sp = 0

    override fun <R> derive(value: R, block: (R) -> Unit): R {
        // save block to stack for backward pass
        if (sp >= stack.size) stack = stack.copyOf(stack.size * 2)
        stack[sp++] = block
        stack[sp++] = value
        return value
    }

    @Suppress("UNCHECKED_CAST")
    fun runBackwardPass() {
        while (sp > 0) {
            val value = stack[--sp]
            val block = stack[--sp] as (Any?) -> Unit
            block(value)
        }
    }
}

// Extensions for differentiation of various basic mathematical functions


// x ^ 2
fun AD.sqr(x: D): D = derive(D(x.x * x.x)) { z ->
    x.d += z.d * 2.0 * x.x
}

// x ^ 1/2
fun AD.sqrt(x: D): D = derive(D(x.x.sqrt)) { z ->
    x.d += z.d * 0.5 / z.x
}

// x ^ y (const)
fun AD.pow(x: D, y: Double): D = derive(D(x.x.pow(y))) { z ->
    x.d += z.d * y * x.x.pow(y - 1)
}


fun AD.pow(x: D, y: Int): D = pow(x, y.toDouble())

// exp(x)
fun AD.exp(x: D): D = derive(D(x.x.exp)) { z ->
    x.d += z.d * z.x
}

// ln(x)
/*
fun AD.ln(x: D): D = derive(D(x.x.ln())) { z ->
    x.d += z.d / x.x
}

 */

// x ^ y (any)
fun AD.pow(x: D, y: D): D = D(( y.x * x.x.ln).exp)



// sin(x)
fun AD.sin(x: D): D = derive(D(x.x.sin)) { z ->
    x.d += z.d * x.x.cos
}

// cos(x)
fun AD.cos(x: D): D = derive(D(x.x.cos)) { z ->
    x.d -= z.d * x.x.sin
}

