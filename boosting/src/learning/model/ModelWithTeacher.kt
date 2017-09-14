package learning.model

import data.Data
import data.RegressionData

interface ModelWithTeacher {
    fun train(data: RegressionData)

    fun trainAll(data: List<RegressionData>)

    fun output(data: Data): DoubleArray
}
