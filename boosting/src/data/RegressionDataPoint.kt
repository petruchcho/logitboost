package data

class RegressionDataPoint(private val x: Data, private val y: Double) : RegressionData {

    override fun asVector(): DoubleArray {
        return x.asVector()
    }

    override fun output(): Double {
        return y
    }
}
