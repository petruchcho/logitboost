package learning.classification

import data.ClassifiedData
import data.Data
import data.RegressionData
import data.RegressionDataDecorator
import learning.model.ModelWithTeacher
import java.util.*

class ClassificationModel(private val model: ModelWithTeacher, private val outputToClass: (DoubleArray) -> Int) : Classifier {

    override fun classify(data: Data): Int {
        return outputToClass(output(data))
    }

    fun trainClassified(data: List<ClassifiedData>) {
        val regressionData = ArrayList<RegressionData>()
        for (classifiedData in data) {
            regressionData.add(RegressionDataDecorator(classifiedData))
        }
        model.trainAll(regressionData)
    }

    override fun train(data: RegressionData) {
        model.train(data)
    }

    override fun trainAll(data: List<RegressionData>) {
        throw RuntimeException("Classification model should be trained on class-labeled data")
    }

    override fun output(data: Data): DoubleArray {
        return model.output(data)
    }
}
