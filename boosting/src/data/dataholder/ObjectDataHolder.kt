package data.dataholder

import data.Data
import data.DataReader

class ObjectDataHolder<T : Data>(dataReader: DataReader<T>, trainPercent: Double, normalize: Boolean, shuffle: Boolean) : DataHolder<T>(dataReader, trainPercent, normalize, shuffle) {

    override fun normalize() {
        val vectorSize = data[0].asVector().size
        for (i in 0..vectorSize - 1) {
            var min = java.lang.Double.POSITIVE_INFINITY
            var max = java.lang.Double.NEGATIVE_INFINITY
            for (data in this.data) {
                min = Math.min(min, data.asVector()[i])
                max = Math.max(max, data.asVector()[i])
            }
            for (data in this.data) {
                val value = data.asVector()[i]
                data.asVector()[i] = (value - min) / (max - min)
            }
        }
    }

    override val vectorSize: Int
        get() {
            return data[0].asVector().size
        }
}
