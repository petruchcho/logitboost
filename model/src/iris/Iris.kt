package iris

import data.ClassifiedData

class Iris(sepalLength: Double, sepalWidth: Double, petalLength: Double, petalWidth: Double, private val irisClass: Iris.IrisClass) : ClassifiedData {

    enum class IrisClass {
        SETOSA,
        VERSICOLOUR,
        VIRGINICA
    }

    private val vector: DoubleArray

    init {
        vector = doubleArrayOf(sepalLength, sepalWidth, petalLength, petalWidth)
    }

    override fun asVector(): DoubleArray {
        return vector
    }

    override val classId: Int
        get() {
            when (irisClass) {
                Iris.IrisClass.SETOSA -> return 0
                Iris.IrisClass.VERSICOLOUR -> return 1
                Iris.IrisClass.VIRGINICA -> return 2
                else -> throw RuntimeException("Unexpected class")
            }
        }
}
