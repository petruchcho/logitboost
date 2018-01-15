package test

import data.DataWithResult
import data.dataholder.ClassifiedDataHolder
import data.dataholder.ObjectDataHolder
import data.deductionsConverter
import data.windowConverter
import finance.FinanceReader
import learning.*
import learning.model.ModelWithTeacher
import learning.regressors.LeastSquareRegressor
import java.io.PrintWriter

const val WINDOW_SIZE = 14

const val TRAIN_PERCENT = 60
const val VALIDATION_PERCENT = 20

const val ITERATIONS = 800

val reader = FinanceReader(windowConverter(WINDOW_SIZE, deductionsConverter))
val dataHolder = ClassifiedDataHolder(reader, TRAIN_PERCENT.toDouble(), normalize = false, shuffle = true, validationPercent = VALIDATION_PERCENT)

fun main(args: Array<String>) {

    dataHolder.init()

    println(dataHolder.data.size)
    println(dataHolder.trainData.size)
    println(dataHolder.testData.size)
    println(dataHolder.validationData.size)

//    runLogitBoost("default", logitFunction = defaultLogitFunction)
//
//    runLogitBoost("floor c = 0.001", logitFunction = floorLogitFunctionSingleC(0.001))
//    runLogitBoost("floor c = 0.01", logitFunction = floorLogitFunctionSingleC(0.01))
//    runLogitBoost("floor c = 0.25", logitFunction = floorLogitFunctionSingleC(0.25))
//    runLogitBoost("floor c = 0.5", logitFunction = floorLogitFunctionSingleC(0.5))
//
//    runLogitBoost("ceil c = 0.001", logitFunction = ceilLogitFunctionSingleC(0.001))
//    runLogitBoost("ceil c = 0.01", logitFunction = ceilLogitFunctionSingleC(0.01))
//    runLogitBoost("ceil c = 0.25", logitFunction = ceilLogitFunctionSingleC(0.25))
//    runLogitBoost("ceil c = 0.5", logitFunction = ceilLogitFunctionSingleC(0.5))
//
//    runLogitBoost("both c = 0.001", logitFunction = bothLogitFunctionSingleC(0.001))
//    runLogitBoost("both c = 0.01", logitFunction = bothLogitFunctionSingleC(0.01))
//    runLogitBoost("both c = 0.25", logitFunction = bothLogitFunctionSingleC(0.25))
//    runLogitBoost("both c = 0.5", logitFunction = bothLogitFunctionSingleC(0.5))
}

fun runLogitBoost(name: String, logitFunction: ((Double) -> Double)) {
    val logitBoost = LogitBoost { LeastSquareRegressor() }
    logitBoost.logitFunction = logitFunction
    PrintWriter(name + ".txt").use { out ->
        for (iterations in 0..ITERATIONS) {
            if (iterations % 100 == 0) {
                System.err.println("$name $iterations/$ITERATIONS iterations completed")
            }
            logitBoost.train(dataHolder.trainData)
            out.println(calcError(logitBoost, dataHolder.trainData))
        }
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
