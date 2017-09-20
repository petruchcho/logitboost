package data

open class Data(open val vector: DoubleArray) {
}

open class DataWithResult(vector: DoubleArray, open val result: Double) : Data(vector) {
}

class WeightedData(vector: DoubleArray, result: Double, private var weight: Double = 1.0) : DataWithResult(vector, result) {
    override val result: Double get() = weight * super.result.toDouble()
}