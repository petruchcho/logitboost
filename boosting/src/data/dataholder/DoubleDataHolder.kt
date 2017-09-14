package data.dataholder

import data.DataReader

class DoubleDataHolder(dataReader: DataReader<Double>, trainPercent: Double, normalize: Boolean, shuffle: Boolean) : DataHolder<Double>(dataReader, trainPercent, normalize, shuffle) {

    override fun normalize() {
        var min = java.lang.Double.POSITIVE_INFINITY
        var max = java.lang.Double.NEGATIVE_INFINITY
        for (x in data) {
            min = Math.min(x, min)
            max = Math.max(x, max)
        }
        for (i in 0..data.size - 1) {
            data[i] = (data[i] - min) / (max - min)
        }
    }

    override val vectorSize: Int
        get() = 1
}
