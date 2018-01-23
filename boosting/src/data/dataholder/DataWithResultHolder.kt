package data.dataholder

import data.DataReader
import data.DataWithResult
import data.splitDataWithPercent
import data.splitToMap

open class DataWithResultHolder(dataReader: DataReader<DataWithResult>, trainPercent: Double, normalize: Boolean, shuffle: Boolean, override val maxSize: Int = Int.MAX_VALUE) : ObjectDataHolder<DataWithResult>(dataReader, trainPercent, normalize, shuffle, maxSize) {
}

class ClassifiedDataHolder(dataReader: DataReader<DataWithResult>, trainPercent: Double, normalize: Boolean, shuffle: Boolean, override val maxSize: Int = Int.MAX_VALUE) : DataWithResultHolder(dataReader, trainPercent, normalize, shuffle, maxSize) {
    override fun postRead() {
        super.postRead()
        val classes = splitToMap(data)
        val size = classes.minBy { it.size }?.size
        data.clear()
        classes.forEach { data.addAll(it.subList(0, size!!)) }
    }

    override fun splitData() {
        val (t1, t2) = splitDataWithPercent(data, trainPercent)
        trainData.addAll(t1)
        testData.addAll(t2)
    }
}