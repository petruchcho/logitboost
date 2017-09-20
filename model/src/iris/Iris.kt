package iris

import data.DataWithResult

class Iris(sepalLength: Double,
           sepalWidth: Double,
           petalLength: Double,
           petalWidth: Double,
           private val irisClass: Iris.IrisClass) : DataWithResult(doubleArrayOf(sepalLength, sepalWidth, petalLength, petalWidth), 0.0) {

    enum class IrisClass {
        SETOSA,
        VERSICOLOUR,
        VIRGINICA
    }

    override val result: Double
        get() {
            when (irisClass) {
                Iris.IrisClass.SETOSA -> return 0.0
                Iris.IrisClass.VERSICOLOUR -> return 1.0
                Iris.IrisClass.VIRGINICA -> return 2.0
                else -> throw RuntimeException("Unexpected class")
            }
        }
}
