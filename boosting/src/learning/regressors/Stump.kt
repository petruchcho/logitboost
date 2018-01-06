package learning.regressors

import data.Data
import data.DataWithResult
import learning.model.Classifier

class Stump(val posLabel: Int, val negLabel: Int) : Classifier {

    var threshold: Double = Double.NaN
    var featureIndex: Int = -1

    override fun classify(data: Data): Int {
        return Math.round(output(data)).toInt()
    }

    override fun output(data: Data): Double {
        if (threshold.isNaN()) throw RuntimeException("Stump was not trained")
        return if (data.vector[featureIndex] >= threshold) posLabel.toDouble() else negLabel.toDouble()
    }

    override fun train(data: List<DataWithResult>) {
        var minError = Double.MAX_VALUE
        for (featureIndex in 0..data[0].vector.size - 1) {
            val (threshold, error) = bestThreshold(data, featureIndex)
            if (minError > error) {
                minError = error
                this.threshold = threshold
                this.featureIndex = featureIndex
            }
        }
    }
}

fun bestThreshold(data: List<DataWithResult>, featureIndex: Int): Pair<Double, Double> {
    var minError = Double.MAX_VALUE
    var resultThreshold = Double.NaN

    val thresholds = data.map { d -> d.vector[featureIndex] }
    for (threshold in thresholds) {
        val error = minLabelErrorOfHypothesisAndNegation(data, { v -> if (v[featureIndex] >= threshold) 1 else -1 })
        if (minError > error) {
            minError = error
            resultThreshold = threshold
        }
    }

    return Pair(resultThreshold, minError)
}

fun minLabelErrorOfHypothesisAndNegation(data: List<DataWithResult>, h: (DoubleArray) -> Int): Double {
    val posData = data.filter { d -> h(d.vector) == 1 }
    val negData = data.filter { d -> h(d.vector) == -1 }

    val posSum = posData.filter { d -> Math.round(d.result) == -1L }.size + negData.filter { d -> Math.round(d.result) == 1L }.size
    val negSum = posData.filter { d -> Math.round(d.result) == 1L }.size + negData.filter { d -> Math.round(d.result) == -1L }.size

    return Math.min(posSum, negSum).toDouble() / data.size
}