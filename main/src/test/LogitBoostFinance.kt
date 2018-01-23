package test

import data.*
import data.dataholder.ClassifiedDataHolder
import finance.FinanceReader
import learning.LogitBoost
import learning.bothLogitFunction
import learning.ceilLogitFunctionSingleC
import learning.defaultLogitFunction
import learning.model.ModelWithTeacher
import learning.regressors.LeastSquareRegressor
import java.io.PrintWriter
import java.util.*

const val WINDOW_SIZE = 40

const val TRAIN_PERCENT = 80
const val VALIDATION_PERCENT = 25
const val C_STEP = 0.001

const val VALIDATION_ITERATIONS = 4
const val ITERATIONS = 250

val reader = FinanceReader(windowConverter(WINDOW_SIZE, deductionsConverter))
val dataHolder = ClassifiedDataHolder(reader, TRAIN_PERCENT.toDouble(), normalize = false, shuffle = true, maxSize = 5000)

fun main(args: Array<String>) {

    dataHolder.init()

//    val defaultLogitBoost = makeLogitBoost()
//    testLogitBoost(defaultLogitBoost, dataHolder.trainData, dataHolder.testData, "default", ITERATIONS)

    //val (cg, cf) = crossValidateCgCf()
    val boostedLogitBoost = makeLogitBoost(getLogitFunction(0.062, 0.013))
    testLogitBoost(boostedLogitBoost, dataHolder.trainData, dataHolder.testData, "boosted", ITERATIONS)
}

fun getLogitFunction(c: Double): ((Double) -> Double) {
    return ceilLogitFunctionSingleC(c)
}

fun getLogitFunction(cg: Double, cf: Double): ((Double) -> Double) {
    return bothLogitFunction(cg, cf)
}

fun makeLogitBoost(logitFunction: ((Double) -> Double) = defaultLogitFunction): LogitBoost {
    val logitBoost = LogitBoost { LeastSquareRegressor() }
    logitBoost.logitFunction = logitFunction
    return logitBoost
}

fun crossValidateC(): Double {
    var bestC = 0.0
    var bestQuality = 0.0

    var c = 0.0
    while (c <= 0.25) {
        System.err.println("test c = $c")
        var sumQuality = 0.0
        for (it in 0..VALIDATION_ITERATIONS - 1) {
            System.err.println("test c = $c, validation iteration = $it")
            Collections.shuffle(dataHolder.trainData)
            val (trainData, validationData) = splitDataWithPercent(dataHolder.trainData, VALIDATION_PERCENT.toDouble())
            val logitBoost = makeLogitBoost(getLogitFunction(c))
            trainLogitBoost(logitBoost, trainData)
            sumQuality += calcQuality(logitBoost, validationData)
        }
        val avgQuality = sumQuality / VALIDATION_ITERATIONS
        if (bestQuality < avgQuality) {
            System.err.println("new best quality = $avgQuality with c = $c")
            bestQuality = avgQuality
            bestC = c
        }

        c += C_STEP
    }
    System.err.println("best c = $bestC")
    return bestC
}

fun crossValidateCgCf(): Pair<Double, Double> {
    var bestC = Pair(0.0, 0.0)
    var bestQuality = 0.0

    var cg = 0.0
    while (cg <= 0.25) {
        var cf = 0.0
        while (cf <= 0.25) {
            System.err.println("test cg = $cg, cf = $cf")
            var sumQuality = 0.0
            for (it in 0..VALIDATION_ITERATIONS - 1) {
                System.err.println("validation iteration = $it")
                Collections.shuffle(dataHolder.trainData)
                val (trainData, validationData) = splitDataWithPercent(dataHolder.trainData, VALIDATION_PERCENT.toDouble())
                val logitBoost = makeLogitBoost(getLogitFunction(cg, cf))
                trainLogitBoost(logitBoost, trainData)
                sumQuality += calcQuality(logitBoost, validationData)
            }
            val avgQuality = sumQuality / VALIDATION_ITERATIONS
            if (bestQuality < avgQuality) {
                System.err.println("new best quality = $avgQuality with cg = $cg, cf = $cf")
                bestQuality = avgQuality
                bestC = Pair(cg, cf)
            }

            cf += C_STEP
        }
        cg += C_STEP
    }
    System.err.println("best c = $bestC")
    return bestC
}

fun trainLogitBoost(logitBoost: LogitBoost, trainData: List<DataWithResult>): LogitBoost {
    for (iterations in 0..ITERATIONS) {
        logitBoost.train(trainData)
    }
    return logitBoost
}

fun testLogitBoost(logitBoost: LogitBoost, trainData: List<DataWithResult>, testData: List<DataWithResult>, filename: String, iterations: Int): LogitBoost {
    PrintWriter(filename + "train.txt").use { out ->
        for (iteration in 0..iterations - 1) {
            if (iteration % 100 == 0) {
                System.err.println("$filename $iteration/$iterations iterations completed")
            }
            logitBoost.train(trainData)
            val quality = calcQuality(logitBoost, testData)
            out.println(quality)
            System.err.println(quality)
        }
    }

    return logitBoost
}

fun calcQuality(logitBoost: ModelWithTeacher, testData: List<DataWithResult>): Double {
    var correctly = 0
    for (data in testData) {
        val output = logitBoost.output(data)
        //println("" + logitBoost.output(data) + " (" + data.result + ")")
        if (data.result > 0 && output > 0) correctly++
        if (data.result < 0 && output < 0) correctly++
    }
    return 1.0 * correctly / testData.size
}
