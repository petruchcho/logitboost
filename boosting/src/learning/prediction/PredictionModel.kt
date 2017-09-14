package learning.prediction

import data.Data
import data.RegressionData
import learning.model.ModelWithTeacher

class PredictionModel(private val model: ModelWithTeacher) : Predictor {

    override fun trainAll(data: List<RegressionData>) {
        model.trainAll(data)
    }

    override fun train(data: RegressionData) {
        model.train(data)
    }

    override fun output(data: Data): DoubleArray {
        return model.output(data)
    }

    override fun predictNext(vector: DoubleArray): Double {
        return output(object : Data {
            override fun asVector(): DoubleArray {
                return vector
            }
        })[0]
    }
}
