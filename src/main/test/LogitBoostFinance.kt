package main.test

import data.DataWithResult
import data.dataholder.ClassifiedDataHolder
import data.deductionsConverter
import data.splitDataWithPercent
import data.windowConverter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import learning.LogitBoost
import learning.bothLogitFunction
import learning.defaultLogitFunction
import learning.floorLogitFunctionSingleC
import learning.model.ModelWithTeacher
import learning.regressors.LeastSquareRegressor
import timeline.TimelineReader
import java.io.PrintWriter

const val TRAIN_PERCENT = 80
const val VALIDATION_PERCENT = 25
const val C_STEP = 0.01

const val VALIDATION_ITERATIONS = 4

const val FULL_ITERATIONS = 300
const val CROSS_VALIDATION_ITERATIONS = 200
const val Z_MAX = 0.04
const val WINDOW_SIZE = 15

val reader = TimelineReader(timeline.BITCOIN, windowConverter(WINDOW_SIZE, deductionsConverter), 1000)
val dataHolder = ClassifiedDataHolder(reader, TRAIN_PERCENT.toDouble(), normalize = false, shuffle = true)

fun main(args: Array<String>) {

    for (i in 0 until 8) {
        println("Here we are at ${i+1}")

        dataHolder.init() // it will reshuffle everything

        val defaultLogitBoost = makeLogitBoost()
        testLogitBoost(defaultLogitBoost, dataHolder.trainData, dataHolder.testData, "default-${i+1}", FULL_ITERATIONS)

        val (cg, cf) = crossValidateCgCf()
        val boostedLogitBoost = makeLogitBoost(getLogitFunction(cf, cg))
        testLogitBoost(boostedLogitBoost, dataHolder.trainData, dataHolder.testData, "boosted-${i+1}-[$cg, $cf]", FULL_ITERATIONS)
    }
}

fun getLogitFunction(c: Double): ((Double) -> Double) {
    return floorLogitFunctionSingleC(c)
}

fun getLogitFunction(cg: Double, cf: Double): ((Double) -> Double) {
    return bothLogitFunction(cg, cf)
}

fun makeLogitBoost(logitFunction: ((Double) -> Double) = defaultLogitFunction): LogitBoost {
    val logitBoost = LogitBoost(Z_MAX.toDouble()) { LeastSquareRegressor() }
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
        for (it in 0 until VALIDATION_ITERATIONS) {
            System.err.println("test c = $c, validation iteration = $it")
            dataHolder.trainData.shuffle()
            val (trainData, validationData) = splitDataWithPercent(dataHolder.trainData, VALIDATION_PERCENT.toDouble())
            val logitBoost = makeLogitBoost(getLogitFunction(c))
            trainLogitBoost(logitBoost, trainData, CROSS_VALIDATION_ITERATIONS)
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

    runBlocking {
        val jobs = ArrayList<Job>()
        var cg = 0.0
        while (cg <= 0.25) {
            var cf = 0.0
            while (cf <= 0.25) {

                dataHolder.trainData.shuffle()
                val (trainData, validationData) = splitDataWithPercent(dataHolder.trainData, VALIDATION_PERCENT.toDouble())
                val _cg = cg
                val _cf = cf

                jobs.add(launch {
                    System.err.println("test cg = $_cg, cf = $_cf")
                    var sumQuality = 0.0
                    for (it in 0 until VALIDATION_ITERATIONS) {
                        val logitBoost = makeLogitBoost(getLogitFunction(_cg, _cf))
                        trainLogitBoost(logitBoost, trainData, CROSS_VALIDATION_ITERATIONS)
                        sumQuality += calcQuality(logitBoost, validationData)
                    }
                    val avgQuality = sumQuality / VALIDATION_ITERATIONS
                    if (bestQuality < avgQuality) {
                        System.err.println("new best quality = $avgQuality with cg = $_cg, cf = $_cf")
                        bestQuality = avgQuality
                        bestC = Pair(_cg, _cf)
                    }
                })

                cf += C_STEP
            }
            cg += C_STEP
        }

        jobs.forEach { job -> job.join() }
    }

    System.err.println("best c = $bestC")
    return bestC
}

fun trainLogitBoost(logitBoost: LogitBoost, trainData: List<DataWithResult>, iterations: Int): LogitBoost {
    for (iteration in 0 until iterations) {
        logitBoost.train(trainData)
    }
    return logitBoost
}

fun testLogitBoost(logitBoost: LogitBoost, trainData: List<DataWithResult>, testData: List<DataWithResult>, filename: String, iterations: Int): LogitBoost {
    PrintWriter("$filename.txt").use { out ->
        for (iteration in 0 until iterations) {
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
