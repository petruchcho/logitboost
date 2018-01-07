package learning

import data.Data
import data.DataWithResult
import data.WeightedData
import data.buildRandomDataSet
import learning.model.Classifier
import learning.model.ModelWithTeacher
import java.util.*

const val DATA_SET_SIZE = 200

class LogitBoost(private val weakLearnersFactory: () -> ModelWithTeacher) : Classifier {

    private val weakLearners = ArrayList<ModelWithTeacher>()

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
                z = Math.max(-.06, z)
            } else {
                z = Math.min(.06, z)
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
        val fx = F(x)
        val efx = Math.exp(fx)
        val enfx = Math.exp(-fx)
        if (efx.isInfinite() && efx > 0 && enfx < 1e-15) return 1.0
        return efx / (efx + enfx)
    }

    private fun createRegressor() = weakLearnersFactory()
}
