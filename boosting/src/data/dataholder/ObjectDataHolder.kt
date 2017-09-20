package data.dataholder

import data.Data
import data.DataReader

class ObjectDataHolder<T : Data>(dataReader: DataReader<T>, trainPercent: Double, normalize: Boolean, shuffle: Boolean) : DataHolder<T>(dataReader, trainPercent, normalize, shuffle) {

    override fun normalize() {
        for (i in 0..vectorSize - 1) {
            var min = Double.POSITIVE_INFINITY
            var max = Double.NEGATIVE_INFINITY
            for (data in this.data) {
                min = Math.min(min, data.vector[i])
                max = Math.max(max, data.vector[i])
            }
            for (data in this.data) {
                val value = data.vector[i]
                data.vector[i] = (value - min) / (max - min)
            }
        }
    }

    override val vectorSize: Int
        get() {
            return data[0].vector.size
        }
}
