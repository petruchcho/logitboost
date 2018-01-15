package learning

import data.Data
import data.DataWithResult
import data.WeightedData
import learning.model.Classifier
import learning.model.ModelWithTeacher
import java.util.*

class LogitBoost(private val weakLearnersFactory: () -> ModelWithTeacher) : Classifier {

    private val weakLearners = ArrayList<ModelWithTeacher>()
    private val Z_MAX = 0.06

    var logitFunction = defaultLogitFunction

    override fun classify(data: Data): Int {
        return Math.round(output(data)).toInt()
    }

    override fun train(data: List<DataWithResult>) {
        val weightedData = ArrayList<WeightedData>()
        for (point in data) {
            val p = p(point)

            val w = Math.max(p * (1 - p), 2e-15)

            var z = if (point.result > 0) 1 / p else -1 / (1 - p)

            if (z < 0) {
                z = Math.max(-Z_MAX, z)
            } else {
                z = Math.min(Z_MAX, z)
            }

            if (!w.isFinite()) {
                throw RuntimeException()
            }
            weightedData.add(WeightedData(point.vector, z, w))
        }
        val weakLearner = createRegressor()
        //weightedData.sortByDescending { w -> w.weight }
        //weakLearner.train(weightedData.subList(0, DATA_SET_SIZE))
        weakLearner.train(weightedData)
        weakLearners.add(weakLearner)
    }

    override fun output(data: Data): Double {
        return F(data)
    }

    private fun F(data: Data): Double {
        var sum = 0.0
        for (regressor in weakLearners) {
            sum += regressor.output(data)
        }
        return sum * 0.5
    }

    private fun p(x: Data): Double {
        return logitFunction(F(x))
    }

    private fun createRegressor() = weakLearnersFactory()
}

val defaultLogitFunction = { x: Double ->
    val efx = Math.exp(x)
    val enfx = Math.exp(-x)

    if (efx.isInfinite() && efx > 0 && enfx < 1e-15) {
        1.0
    } else if (enfx.isInfinite()) {
        0.0
    } else {
        if (efx.isNaN() || enfx.isNaN() || efx.isInfinite() || enfx.isInfinite()) {
            throw RuntimeException("" + efx + " " + enfx)
        }
        efx / (efx + enfx)
    }
}

fun floorLogitFunctionSingleC(c: Double): ((Double) -> Double) {
    return {
        c + (1 - c) * defaultLogitFunction(it)
    }
}

fun ceilLogitFunctionSingleC(c: Double): ((Double) -> Double) {
    return {
        (1 - c) * defaultLogitFunction(it)
    }
}

fun bothLogitFunctionSingleC(c: Double): ((Double) -> Double) {
    return {
        c + (1 - 2 * c) * defaultLogitFunction(it)
    }
}

