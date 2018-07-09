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
            return when (irisClass) {
                Iris.IrisClass.SETOSA -> 0.0
                Iris.IrisClass.VERSICOLOUR -> 1.0
                Iris.IrisClass.VIRGINICA -> 2.0
            }
        }
}
