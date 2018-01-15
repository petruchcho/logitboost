package data.dataholder

import data.DataReader
import java.io.IOException
import java.util.*

abstract class DataHolder<T>(private val dataReader: DataReader<T>,
                             internal val trainPercent: Double,
                             val normalize: Boolean,
                             val shuffle: Boolean,
                             internal open val maxSize: Int = Int.MAX_VALUE) {


    val data: MutableList<T> = ArrayList()

    val trainData: MutableList<T> = ArrayList()
    val testData: MutableList<T> = ArrayList()
    val validationData: MutableList<T> = ArrayList()

    fun init(): DataHolder<T> {
        readData()
        postRead()

        if (normalize) {
            normalize()
        }

        splitData()

        if (shuffle) {
            Collections.shuffle(data)
            Collections.shuffle(trainData)
            Collections.shuffle(testData)
            Collections.shuffle(validationData)
        }

        return this
    }

    protected open fun splitData() {
        for (data in this.data) {
            if (trainData.size < this.data.size * trainPercent / 100.0) {
                trainData.add(data)
            } else {
                testData.add(data)
            }
        }
    }

    internal open fun postRead() {
    }

    private fun readData() {
        try {
            val data = dataReader.readData()
            this.data.addAll(data.subList(0, Math.min(data.size, maxSize)))
        } catch (e: IOException) {
            System.err.print("Fail in readData = " + e)
        }
    }

    protected abstract fun normalize()

    abstract val vectorSize: Int
}
