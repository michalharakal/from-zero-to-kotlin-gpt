package simple

import org.skainet.Dense
import org.skainet.activations.ReLU
import org.skainet.activations.Tanh
import org.skainet.activations.tanh
import org.skainet.dsl.network
import org.skainet.init.Constant
import org.skainet.init.GlorotNormal
import org.skainet.init.Zeros
import org.skainet.nn.ConvPadding

private const val EPOCHS = 3
private const val TRAINING_BATCH_SIZE = 1000
private const val NUM_CHANNELS = 1L
private const val IMAGE_SIZE = 28L
private const val SEED = 12L
private const val TEST_BATCH_SIZE = 1000

fun main() {
    val lenet5Classic = network {
        input(
            IMAGE_SIZE,
            IMAGE_SIZE,
            NUM_CHANNELS
        )
        conv2D(
            filters = 6,
            kernelSize = intArrayOf(5, 5),
            strides = intArrayOf(1, 1, 1, 1)
        ) {
            activation = tanh
            kernelInitializer = GlorotNormal(SEED.toDouble())
            biasInitializer = Zeros()
            padding = ConvPadding.SAME

        }
        avgPool2D(
            poolSize = intArrayOf(1, 2, 2, 1),
            strides = intArrayOf(1, 2, 2, 1),
            padding = ConvPadding.VALID
        )
        conv2D(
            filters = 16,
            kernelSize = intArrayOf(5, 5),
            strides = intArrayOf(1, 1, 1, 1)
        ) {
            activation = tanh
            kernelInitializer = GlorotNormal(SEED.toDouble())
            biasInitializer = Zeros(),
            padding = ConvPadding.SAME
        }
        avgPool2D(
            poolSize = intArrayOf(1, 2, 2, 1),
            strides = intArrayOf(1, 2, 2, 1),
            padding = ConvPadding.VALID
        )
        flatten() // 3136
        dense(120) {
            activation = tanh
            weights { shape ->
                GlorotNormal(SEED.toDouble()).init(shape)
            }
            bias { shape ->
                Constant(0.1).init(shape)

            }
        }
        dense(84) {
            activation = tanh
            weights { shape ->
                GlorotNormal(SEED.toDouble()).init(shape)
            }
            bias { shape ->
                Constant(0.1).init(shape)

            }
        }
        dense(10) {
            weights { shape ->
                GlorotNormal(SEED.toDouble()).init(shape)
            }
            bias { shape ->
                Constant(0.1).init(shape)

            }
        }
    }
}
