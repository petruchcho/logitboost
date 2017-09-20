package learning

import data.Data
import data.DataWithResult
import data.WeightedData
import learning.model.ModelWithTeacher
import learning.regressors.WeightedRegressor
import java.util.*

class LogitBoost(private val weakLearnersFactory: () -> ModelWithTeacher) : ModelWithTeacher {

    private val weakLearners = ArrayList<ModelWithTeacher>()

    override fun trainAll(data: List<DataWithResult>) {
        val regressionData = ArrayList<DataWithResult>()

        for (point in data) {
            val p = p(point)
            val z = (point.result - p) / (p * (1 - p))
            val w = p * (1 - p)
            regressionData.add(WeightedData(point.vector, z, w))
        }

        val regressor = WeightedRegressor(createRegressor())
        regressor.trainAll(regressionData)
        regressor.setWeight(0.5)
        weakLearners.add(regressor)
    }

    override fun train(data: DataWithResult) {
        throw RuntimeException("Logitboost should know all data set")
    }

    override fun output(data: Data): Double {
        return p(data)
    }

    private fun F(data: Data): Double {
        var sum = 0.0
        for (regressor in weakLearners) {
            sum += regressor.output(data)
        }
        return sum
    }

    private fun p(x: Data): Double {
        val F = F(x)
        return Math.exp(F) / (Math.exp(F) + Math.exp(-F))
    }

    private fun createRegressor() = weakLearnersFactory()
}
