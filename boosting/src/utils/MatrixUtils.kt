package utils

import java.util.Arrays

object MatrixUtils {

    fun multiply(a: DoubleArray, b: DoubleArray): Double {
        if (a.size != b.size) {
            throw RuntimeException()
        }
        var res = 0.0
        for (i in a.indices) {
            res += a[i] * b[i]
        }
        return res
    }

    fun multiply(a: DoubleArray, coeff: Double): DoubleArray {
        val res = Arrays.copyOf(a, a.size)
        for (i in a.indices) {
            res[i] = a[i] * coeff
        }
        return res
    }

    fun subtract(a: DoubleArray, b: DoubleArray): DoubleArray {
        if (a.size != b.size) {
            throw RuntimeException()
        }
        val res = DoubleArray(a.size)
        for (i in a.indices) {
            res[i] = a[i] - b[i]
        }
        return res
    }

    fun add(a: DoubleArray, b: DoubleArray): DoubleArray {
        if (a.size != b.size) {
            throw RuntimeException()
        }
        val res = DoubleArray(a.size)
        for (i in a.indices) {
            res[i] = a[i] + b[i]
        }
        return res
    }
}
