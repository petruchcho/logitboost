package learning.regressors

import data.Data
import data.DataWithResult
import learning.model.ModelWithTeacher

class WeightedRegressor(private val regressor: ModelWithTeacher) : ModelWithTeacher {

    private var weight = 1.0

    override fun train(data: DataWithResult) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun trainAll(data: List<DataWithResult>) {
        regressor.trainAll(data)
    }

    override fun output(data: Data): Double {
        return weight * regressor.output(data).toDouble()
    }

    fun setWeight(weight: Double) {
        this.weight = weight
    }
}
