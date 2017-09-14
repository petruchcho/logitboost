package data

class RegressionDataDecorator(private val classifiedData: ClassifiedData) : RegressionData {

    private var converter: ((Int) -> Double)? = null

    constructor(classifiedData: ClassifiedData, converter: (Int) -> Double) : this(classifiedData) {
        this.converter = converter
    }

    override fun output(): Double {
        return converter?.invoke(classifiedData.classId) ?: classifiedData.classId.toDouble()
    }

    override fun asVector(): DoubleArray {
        return classifiedData.asVector()
    }
}
