//package learning.regressors
//
//import data.Data
//import data.DataWithResult
//import sun.reflect.generics.reflectiveObjects.NotImplementedException
//import java.util.*
//
//class Stump(private val argId: Int) : Regressor {
//
//    /**
//     * It is going to be 1 if v[argId] > t and -1 otherwise.
//     */
//    private var t: Double = 0.toDouble()
//
//    init {
//        throw NotImplementedException()
//    }
//
//    override fun train(data: List<DataWithResult>) {
//        val sortedData = ArrayList(data)
//        sortedData.sortBy { o -> o.asVector()[argId] }
//        var firstClassCount = 0
//        for (dataInstance in sortedData) {
//            if (dataInstance.output() == 1.0) {
//                firstClassCount++
//            }
//        }
//        t = sortedData[0].asVector()[argId] - 0.5
//        var bestCorrect = firstClassCount
//        var currentCorrect = firstClassCount
//        for (dataInstance in sortedData) {
//            if (dataInstance.output() == 1.0) {
//                currentCorrect--
//            } else {
//                currentCorrect++
//            }
//            if (bestCorrect < currentCorrect) {
//                bestCorrect = currentCorrect
//                t = dataInstance.asVector()[argId]
//            }
//        }
//    }
//
//    override fun regress(data: Data): Double {
//        return (if (data.asVector()[argId] > t) 1 else -1).toDouble()
//    }
//}
