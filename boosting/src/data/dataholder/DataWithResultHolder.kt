package data.dataholder

import data.DataReader
import data.DataWithResult
import java.util.*

open class DataWithResultHolder(dataReader: DataReader<DataWithResult>, trainPercent: Double, normalize: Boolean, shuffle: Boolean, override val maxSize: Int = Int.MAX_VALUE) :  ObjectDataHolder<DataWithResult>(dataReader, trainPercent, normalize, shuffle, maxSize) {
}

class ClassifiedDataHolder(dataReader: DataReader<DataWithResult>, trainPercent: Double, normalize: Boolean, shuffle: Boolean, val validationPercent: Int = 0, override val maxSize: Int = Int.MAX_VALUE) :  DataWithResultHolder(dataReader, trainPercent, normalize, shuffle, maxSize) {
    override fun postRead() {
        super.postRead()
        val classes = separateClasses()
        val size = classes.minBy { it.size }?.size
        data.clear()
        classes.forEach { data.addAll(it.subList(0, size!!)) }
    }

    override fun splitData() {
        val classes = separateClasses()
        val border1 = (classes[0].size * trainPercent / 100.0).toInt()
        val border2 = border1 + (classes[0].size * validationPercent / 100.0).toInt()

        classes.forEach {
            trainData.addAll(it.subList(0, border1))
            validationData.addAll(it.subList(border1, border2))
            testData.addAll(it.subList(border2, it.size))
        }
    }

    private fun separateClasses(): List<List<DataWithResult>> {
        val map = HashMap<Int, MutableList<DataWithResult>>()
        for (data in this.data) {
            val key = Math.round(data.result).toInt()
            var list = map[key]
            if (list == null) {
                list = ArrayList<DataWithResult>()
                map.put(key, list)
            }
            list.add(data)
        }
        return map.values.toList()
    }
}