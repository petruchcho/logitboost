package test

import data.DataWithResult
import data.dataholder.ObjectDataHolder
import data.deductionsConverter
import data.windowCoverter
import finance.FinanceReader
import learning.LogitBoost
import learning.regressors.LogisticRegressor

const val WINDOW_SIZE = 10
const val TRAIN_PERCENT = 90

val reader = FinanceReader(0, windowCoverter(WINDOW_SIZE, deductionsConverter))
val dataHolder = ObjectDataHolder(reader, TRAIN_PERCENT.toDouble(), false, false)

fun main(args: Array<String>) {
//    for (data in dataHolder.data) {
//        println(Arrays.toString(data.vector.toTypedArray()) + " " + data.result)
//    }

    val logitBoost = LogitBoost { LogisticRegressor(dataHolder.vectorSize, 0.05, 300) }

    for (iterations in 0..10020) {
        logitBoost.trainAll(dataHolder.getTrainData())
        println(calcError(logitBoost, dataHolder.getTestData()))
    }
}

fun calcError(logitBoost: learning.LogitBoost, testData: List<DataWithResult>): Double {
    var correctly = 0
    for (data in testData) {
        val output = logitBoost.output(data)
        //println(logitBoost.output(data))
        if (data.result > 0 && output > 0) correctly++
        if (data.result < 0 && output < 0) correctly++
    }
    return 1.0 * correctly / testData.size
}
