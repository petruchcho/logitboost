package learning

import data.Data
import data.DataWithResult
import data.WeightedData
import data.buildRandomDataSet
import learning.model.Classifier
import learning.model.ModelWithTeacher
import learning.regressors.WeightedRegressor
import java.util.*

const val DATA_SET_SIZE = 150

class LogitBoost(private val weakLearnersFactory: () -> ModelWithTeacher) : Classifier {

    private val weakLearners = ArrayList<ModelWithTeacher>()

    override fun classify(data: Data): Int {
        return Math.round(output(data)).toInt()
    }

    override fun train(data: List<DataWithResult>) {
        val weightedData = ArrayList<WeightedData>()
        for (point in data) {
            val _y = (point.result + 1) / 2
            val p = p(point)
            val w = p * (1 - p)
            val z = (_y - p) / w
            weightedData.add(WeightedData(point.vector, z, w))
        }
        val weakLearner = createRegressor()
        weakLearner.train(buildRandomDataSet(weightedData, DATA_SET_SIZE))
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
        val F = F(x)
        return Math.exp(F) / (Math.exp(F) + Math.exp(-F))
    }

    private fun createRegressor() = weakLearnersFactory()
}
