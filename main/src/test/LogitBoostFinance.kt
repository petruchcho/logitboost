package test

import data.DataWithResult
import data.dataholder.ObjectDataHolder
import data.deductionsConverter
import data.windowCoverter
import finance.FinanceReader
import learning.LogitBoost
import learning.model.ModelWithTeacher
import learning.regressors.LeastSquareRegressor
import seed.SeedReader

const val WINDOW_SIZE = 14
const val TRAIN_PERCENT = 85

//val reader = SeedReader()

val reader = FinanceReader(0, windowCoverter(WINDOW_SIZE, deductionsConverter))
val dataHolder = ObjectDataHolder(reader, TRAIN_PERCENT.toDouble(), normalize = false, shuffle = true)

fun main(args: Array<String>) {
    val logitBoost = LogitBoost { LeastSquareRegressor() }

    for (iterations in 0..2000) {
        logitBoost.train(dataHolder.getTrainData())
        println(calcError(logitBoost, dataHolder.getTestData()))
    }
}

fun calcError(logitBoost: ModelWithTeacher, testData: List<DataWithResult>): Double {
    var correctly = 0
    for (data in testData) {
        val output = logitBoost.output(data)
        //println("" + logitBoost.output(data) + " (" + data.result + ")")
        if (data.result > 0 && output > 0) correctly++
        if (data.result < 0 && output < 0) correctly++
    }
    return 1 - 1.0 * correctly / testData.size
}
