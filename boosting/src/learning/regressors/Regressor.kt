package learning.regressors

import data.Data
import data.RegressionData

interface Regressor {
    fun train(data: List<RegressionData>)

    fun regress(data: Data): Double
}
