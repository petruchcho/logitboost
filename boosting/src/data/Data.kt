package data

import java.util.*

open class Data(open val vector: DoubleArray) {
}

open class DataWithResult(vector: DoubleArray, open val result: Double) : Data(vector) {
}

class WeightedData(vector: DoubleArray, result: Double, var weight: Double = 1.0) : DataWithResult(vector, result) {
}

fun windowConverter(windowSize: Int, firstApply: (List<Double>) -> List<Double>) = { list: List<Double> ->
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