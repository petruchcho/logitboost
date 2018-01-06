package learning.regressors

import data.Data
import data.DataWithResult
import learning.model.ModelWithTeacher

class WeightedRegressor(private val regressor: ModelWithTeacher) : ModelWithTeacher {

    private var weight = 1.0

    override fun train(data: List<DataWithResult>) {
        regressor.train(data)
    }

    override fun output(data: Data): Double {
        return weight * regressor.output(data).toDouble()
    }

    fun setWeight(weight: Double) {
        this.weight = weight
    }
}
