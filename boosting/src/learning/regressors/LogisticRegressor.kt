package learning.regressors

import data.Data
import data.RegressionData
import utils.LogisticUtils
import utils.MatrixUtils
import utils.RandomUtils

class LogisticRegressor(private val inputSize: Int, private val learningStep: Double, private val iterations: Int) : Regressor {

    private var c: DoubleArray = DoubleArray(inputSize, { RandomUtils.instance.nextDouble() - 0.5 })

    override fun train(data: List<RegressionData>) {
        for (iter in 0..iterations - 1) {
            var sum = DoubleArray(inputSize)
            for (point in data) {
                val x = point.asVector()
                val y = point.output()
                sum = MatrixUtils.add(sum, MatrixUtils.multiply(x, y - regress(point)))
            }
            this.c = MatrixUtils.add(c, MatrixUtils.multiply(sum, learningStep))
        }
    }

    /**
     * Shows probability that class = 1
     */
    override fun regress(data: Data): Double {
        return 2 * f(MatrixUtils.multiply(c, data.asVector())) - 1
    }

    private fun f(z: Double): Double {
        return LogisticUtils.logisticFunction(z)
    }
}
