package learning.classification

import data.Data
import learning.model.ModelWithTeacher

interface Classifier : ModelWithTeacher {
    fun classify(data: Data): Int
}
