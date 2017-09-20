package learning.regressors

import data.Data
import data.DataWithResult
import learning.model.Classifier
import learning.model.ModelWithTeacher
import utils.LogisticUtils
import utils.MatrixUtils
import utils.RandomUtils

open class LogisticRegressor(private val inputSize: Int, private val learningStep: Double, private val iterations: Int) : ModelWithTeacher {

    private val SELECTION_POINT = 0.5

    private var c: DoubleArray = DoubleArray(inputSize, { RandomUtils.instance.nextDouble() - 0.5 })

    override fun output(data: Data): Double {
        return if (probability(data) > SELECTION_POINT) 1.0 else 0.0
    }

    override fun train(data: DataWithResult) {
        throw RuntimeException("Call trainAll instead")
    }

    override fun trainAll(data: List<DataWithResult>) {
        for (iter in 0..iterations - 1) {
            var sum = DoubleArray(inputSize)
            for (point in data) {
                val x = point.vector
                val y = point.result
                sum = MatrixUtils.add(sum, MatrixUtils.multiply(x, y - probability(point)))
            }
            this.c = MatrixUtils.add(c, MatrixUtils.multiply(sum, learningStep))
        }
    }

    /**
     * Shows probability that class = 1
     */
    private fun probability(data: Data): Double {
        return 2 * f(MatrixUtils.multiply(c, data.vector)) - 1
    }

    private fun f(z: Double): Double {
        return LogisticUtils.logisticFunction(z)
    }
}
