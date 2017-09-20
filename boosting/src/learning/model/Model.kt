package learning.model

import data.Data
import data.DataWithResult

interface ModelWithTeacher {
    fun train(data: DataWithResult)

    fun trainAll(data: List<DataWithResult>)

    fun output(data: Data): Double
}

interface Classifier : ModelWithTeacher {
    fun classify(data: Data): Int {
        return output(data).toInt()
    }
}

class PredictionModel(private val model: ModelWithTeacher) : ModelWithTeacher {

    override fun trainAll(data: List<DataWithResult>) {
        model.trainAll(data)
    }

    override fun train(data: DataWithResult) {
        model.train(data)
    }

    override fun output(data: Data): Double {
        return model.output(data)
    }

    fun predictNext(vector: Data): Double {
        return output(vector)
    }
}

class ClassificationModel(private val model: ModelWithTeacher) : Classifier {

    override fun train(data: DataWithResult) {
        model.train(data)
    }

    override fun trainAll(data: List<DataWithResult>) {
        model.trainAll(data)
    }

    override fun output(data: Data): Double {
        return model.output(data)
    }
}