package test

import data.DataWithResult
import data.dataholder.ObjectDataHolder
import iris.Iris
import iris.IrisReader
import learning.LogitBoost
import learning.regressors.LogisticRegressor
import seed.SeedReader
import java.util.*

object LogitBoostClassificationTest { // TODO Rewrite it

    private val TRAIN_PERCENT = 80

    @JvmStatic fun main(args: Array<String>) {
        val dataHolder = ObjectDataHolder(SeedReader(), TRAIN_PERCENT.toDouble(), true, false)

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
            if (trainData.size * 100 < filteredData.size * TRAIN_PERCENT) {
                trainData.add(data)
            } else {
                testData.add(data)
            }
        }

        val logitBoost = LogitBoost { LogisticRegressor(dataHolder.vectorSize, .001, 60) }

        for (it in 0..100) {
            logitBoost.trainAll(trainData)
        }

        for (data in trainData) {
            System.err.printf("{%s} -> %.5f\n", data.result, logitBoost.output(data))
        }
    }
}
