package org.skainet.loss

import de.jugda.knanogpt.core.tensor.Tensor
import org.skainet.nn.Module
import kotlin.math.log

fun crossEntropy(predictions: Tensor, labels: Tensor): Double {
    val numClasses = predictions.shape.dimensions.last()
    val batchSize = predictions.shape.dimensions.fold(1) { acc, dim -> acc * dim } / numClasses

    var lossSum = 0.0
    for (i in 0 until batchSize) {
        for (j in 0 until numClasses) {
            val predictionIndex = i * numClasses + j
            val labelIndex = i * numClasses + j
            val predictedProbability = predictions.elements[predictionIndex]
            val trueLabel = labels.elements[labelIndex]

            if (trueLabel == 1.0) { // assuming labels are one-hot encoded
                lossSum -= log(predictedProbability, kotlin.math.E)
            }
        }
    }

    return lossSum / batchSize
}

class CrossEntropyModule(
    calcModule: Module,
    override val name: String
) : LossWrapperModule(calcModule, ::crossEntropy, name)