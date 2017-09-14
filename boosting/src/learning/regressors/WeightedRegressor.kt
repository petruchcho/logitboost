package learning.regressors

import data.Data
import data.RegressionData

class WeightedRegressor(private val regressor: Regressor) : Regressor {
    private var weight = 1.0

    override fun train(data: List<RegressionData>) {
        regressor.train(data)
    }

    override fun regress(data: Data): Double {
        return weight * regressor.regress(data)
    }

    fun setWeight(weight: Double) {
        this.weight = weight
    }
}
