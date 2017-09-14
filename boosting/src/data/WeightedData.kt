package data

class WeightedData @JvmOverloads constructor(private val data: RegressionData, private var weight: Double = 1.0) : RegressionData {

    fun setWeight(weight: Double) {
        this.weight = weight
    }

    override fun asVector(): DoubleArray {
        return data.asVector()
    }

    override fun output(): Double {
        return data.output() * weight
    }
}
