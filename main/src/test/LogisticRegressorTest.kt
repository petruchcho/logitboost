package test

import data.DataWithResult
import data.dataholder.ObjectDataHolder
import learning.regressors.LogisticRegressor
import seed.SeedReader
import java.util.*

object LogisticRegressorTest {

    private val TRAIN_PERCENT = 90

    @JvmStatic fun main(args: Array<String>) {
        val dataHolder = ObjectDataHolder(SeedReader(), TRAIN_PERCENT.toDouble(), true, true)

        val trainData = ArrayList<DataWithResult>()
        val testData = ArrayList<DataWithResult>()

        val filteredData = ArrayList<DataWithResult>()
        for (data in dataHolder.data) {
            if (data.result.toInt() != 2) {
                filteredData.add(data)
            }
        }

        Collections.shuffle(filteredData)
        for (data in filteredData) {
            if (trainData.size < filteredData.size * TRAIN_PERCENT / 100.0) {
                trainData.add(data)
            } else {
                testData.add(data)
            }
        }

        val regressor = LogisticRegressor(dataHolder.vectorSize, 0.001, 30)
        regressor.train(trainData)

        var correctly = 0
        for (data in testData) {
            if (data.result.toInt() == regressor.output(data).toInt()) {
                correctly++
            }
        }
        System.err.printf("Correctly = %.2f\n", 100.0 * correctly / testData.size)
    }
}
