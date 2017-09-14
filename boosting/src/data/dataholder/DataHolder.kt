package data.dataholder

import data.DataReader
import java.io.IOException
import java.util.*

abstract class DataHolder<T>(private val dataReader: DataReader<T>, private val trainPercent: Double, normalize: Boolean, shuffle: Boolean) {

    val data: MutableList<T> = ArrayList()

    private val trainData: MutableList<T> = ArrayList()
    private val testData: MutableList<T> = ArrayList()

    init {
        readData()

        if (normalize) {
            @Suppress("LeakingThis")
            normalize()
        }

        if (shuffle) {
            Collections.shuffle(data)
        }

        splitData()
    }

    private fun splitData() {
        for (data in this.data) {
            if (trainData.size < this.data.size * trainPercent / 100.0) {
                trainData.add(data)
            } else {
                testData.add(data)
            }
        }
    }

    private fun readData() {
        try {
            data.addAll(dataReader.readData())
        } catch (e: IOException) {
            System.err.print("Fail in readData = " + e)
        }
    }

    fun getTestData(): List<T> {
        return testData
    }

    fun getTrainData(): List<T> {
        return trainData
    }

    protected abstract fun normalize()

    abstract val vectorSize: Int
}
