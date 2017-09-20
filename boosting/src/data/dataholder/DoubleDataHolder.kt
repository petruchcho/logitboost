package data.dataholder

import data.DataReader

class DoubleDataHolder(dataReader: DataReader<Double>, trainPercent: Double, normalize: Boolean, shuffle: Boolean) : DataHolder<Double>(dataReader, trainPercent, normalize, shuffle) {

    override fun normalize() {
        var min = Double.POSITIVE_INFINITY
        var max = Double.NEGATIVE_INFINITY
        for (x in data) {
            min = Math.min(x, min)
            max = Math.max(x, max)
        }
        data.forEach { x -> x - min }
        if (min != max) {
            data.forEach { x -> x / (max - min) }
        }
    }

    override val vectorSize: Int
        get() = 1
}
