package learning.prediction

import learning.model.ModelWithTeacher

interface Predictor : ModelWithTeacher {
    fun predictNext(vector: DoubleArray): Double
}
