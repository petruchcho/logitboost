package test

import data.ClassifiedData
import data.RegressionData
import data.RegressionDataDecorator
import data.dataholder.ObjectDataHolder
import learning.regressors.LogisticRegressor
import seed.SeedReader
import java.util.*

object LogisticRegressorTest {

    private val TRAIN_PERCENT = 90

    @JvmStatic fun main(args: Array<String>) {
        val dataHolder = ObjectDataHolder(SeedReader(), TRAIN_PERCENT.toDouble(), true, true)

        val trainData = ArrayList<RegressionData>()
        val testData = ArrayList<ClassifiedData>()

        val filteredData = ArrayList<ClassifiedData>()
        for (data in dataHolder.data) {
            if (data.classId != 2) {
                filteredData.add(data)
            }
        }

        Collections.shuffle(filteredData)
        for (data in filteredData) {
            if (trainData.size < filteredData.size * TRAIN_PERCENT / 100.0) {
                trainData.add(RegressionDataDecorator(data))
            } else {
                testData.add(data)
            }
        }


        val regressor = LogisticRegressor(dataHolder.vectorSize, 0.01, 2000)
        regressor.train(trainData)

        var correctly = 0
        for (data in testData) {
            val regress = regressor.regress(data)
            if (regress > 1 - regress) {
                if (data.classId == 1) {
                    correctly++
                }
            } else {
                if (data.classId == 0) {
                    correctly++
                }
            }
        }
        System.err.printf("Correctly = %.2f\n", 100.0 * correctly / testData.size)
    }
}
