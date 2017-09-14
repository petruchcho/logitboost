package seed


/**
 * 1. area A,
 * 2. perimeter P,
 * 3. compactness C = 4*pi*A/P^2,
 * 4. length of kernel,
 * 5. width of kernel,
 * 6. asymmetry coefficient
 * 7. length of kernel groove.
 */

class Seed(private val area: Double, private val perimeter: Double, private val compactness: Double, private val kernelLength: Double, private val kernelWidth: Double, private val asymmetryCoefficient: Double, private val kernelGrooveLength: Double, private val newClassId: Int) : data.ClassifiedData {

    private val vector: DoubleArray

    init {
        this.vector = makeVector()
    }

    override fun asVector(): DoubleArray {
        return vector
    }

    private fun makeVector(): DoubleArray {
        return doubleArrayOf(area, perimeter, compactness, kernelLength, kernelWidth, asymmetryCoefficient, kernelGrooveLength)
    }

    override val classId: Int get() = newClassId - 1
}
