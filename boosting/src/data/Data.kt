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

fun splitToMap(data: List<DataWithResult>): List<List<DataWithResult>> {
    val map = HashMap<Int, MutableList<DataWithResult>>()
    for (d in data) {
        val key = Math.round(d.result).toInt()
        var list = map[key]
        if (list == null) {
            list = ArrayList<DataWithResult>()
            map.put(key, list)
        }
        list.add(d)
    }
    return map.values.toList()
}

fun splitDataWithPercent(data: List<DataWithResult>, percent: Double): Pair<List<DataWithResult>, List<DataWithResult>> {
    val classes = splitToMap(data)
    val border = (classes[0].size * percent / 100.0).toInt()

    val list1 = ArrayList<DataWithResult>()
    val list2 = ArrayList<DataWithResult>()

    classes.forEach {
        list1.addAll(it.subList(0, border))
        list2.addAll(it.subList(border, it.size))
    }
    return Pair(list1, list2)
}