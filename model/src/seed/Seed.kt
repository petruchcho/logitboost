package seed

import data.DataWithResult


/**
 * 1. area A,
 * 2. perimeter P,
 * 3. compactness C = 4*pi*A/P^2,
 * 4. length of kernel,
 * 5. width of kernel,
 * 6. asymmetry coefficient
 * 7. length of kernel groove.
 */

class Seed(area: Double,
           perimeter: Double,
           compactness: Double,
           kernelLength: Double,
           kernelWidth: Double,
           asymmetryCoefficient: Double,
           kernelGrooveLength: Double,
           newClassId: Int) : DataWithResult(doubleArrayOf(area, perimeter, compactness, kernelLength, kernelWidth, asymmetryCoefficient, kernelGrooveLength), (newClassId - 1).toDouble())
