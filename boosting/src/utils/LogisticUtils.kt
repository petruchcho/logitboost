package utils

object LogisticUtils {

    fun logisticFunction(t: Double): Double {
        return 1 / (1 + Math.exp(-t))
    }
}
