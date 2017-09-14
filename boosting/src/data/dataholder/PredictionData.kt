package data.dataholder

import data.RegressionData

class PredictionData(private val window: DoubleArray, private val expectedResult: Double) : RegressionData {

    override fun output(): Double {
        return expectedResult
    }

    override fun asVector(): DoubleArray {
        return window
    }
}
