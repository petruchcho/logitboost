package data

import utils.RandomUtils
import java.util.*

open class Data(open val vector: DoubleArray) {
}

open class DataWithResult(vector: DoubleArray, open val result: Double) : Data(vector) {
}

class WeightedData(vector: DoubleArray, result: Double, var weight: Double = 1.0) : DataWithResult(vector, result) {
}

fun windowCoverter(windowSize: Int, firstApply: (List<Double>) -> List<Double>) = { list: List<Double> ->
    val result = ArrayList<DataWithResult>()
    val modifiedList = firstApply(list)
    for (i in modifiedList.indices) {
        if (i >= windowSize) {
            result.add(DataWithResult(DoubleArray(windowSize, { j -> modifiedList[i - windowSize + j] }), modifiedList[i]))
        }
    }
    result
}

val deductionsConverter = { list: List<Double> ->
    DoubleArray(list.size - 1, { i -> list[i + 1] - list[i] }).map { x -> if (x > 0) 1.0 else -1.0 }.toList()
}

fun buildRandomDataSet(data: List<WeightedData>, size: Int): List<WeightedData> {
    val result = ArrayList<WeightedData>()
    val weightSum = data.sumByDouble { d -> d.weight }
    while (result.size < size) {
        result.add(randomElement(data, weightSum))
    }
    return result
}

fun randomElement(data: List<WeightedData>, weightSum: Double): WeightedData {
//    var random = RandomUtils.instance.nextDouble(weightSum)
//    for (d in data) {
//        random -= d.weight
//        if (random < 0) {
//            return d
//        }
//    }
//    return data.last()
    return data[RandomUtils.instance.nextInt(data.size)]
}