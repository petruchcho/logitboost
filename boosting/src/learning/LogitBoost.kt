package learning

import data.Data
import data.RegressionData
import data.RegressionDataPoint
import data.WeightedData
import learning.model.ModelWithTeacher
import learning.regressors.Regressor
import learning.regressors.WeightedRegressor
import java.util.*

class LogitBoost(private val regressorFactory: () -> Regressor) : ModelWithTeacher {

    private val weakLearners = ArrayList<Regressor>()

    override fun trainAll(data: List<RegressionData>) {
        val regressionData = ArrayList<RegressionData>()

        for (point in data) {
            val p = p(point)
            val z = (point.output() - p) / (p * (1 - p))
            val w = p * (1 - p)
            regressionData.add(WeightedData(RegressionDataPoint(point, z), w))
        }

        val regressor = WeightedRegressor(createRegressor())
        regressor.train(regressionData)
        regressor.setWeight(0.5)
        weakLearners.add(regressor)
    }

    override fun train(data: RegressionData) {
        throw RuntimeException("Logitboost should know all data set")
    }

    override fun output(data: Data): DoubleArray {
        return doubleArrayOf(p(data))
    }

    private fun F(data: Data): Double {
        var sum = 0.0
        for (regressor in weakLearners) {
            sum += regressor.regress(data)
        }
        return sum
    }

    private fun p(x: Data): Double {
        val F = F(x)
        return Math.exp(F) / (Math.exp(F) + Math.exp(-F))
    }

    private fun createRegressor(): Regressor {
        return regressorFactory()
    }
}
