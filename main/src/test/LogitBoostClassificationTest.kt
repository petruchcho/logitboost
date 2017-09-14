package test

import data.ClassifiedData
import data.dataholder.ObjectDataHolder
import learning.LogitBoost
import learning.classification.ClassificationModel
import learning.regressors.LogisticRegressor
import seed.SeedReader
import java.util.*

object LogitBoostClassificationTest {

    private val TRAIN_PERCENT = 80

    @JvmStatic fun main(args: Array<String>) {
        val dataHolder = ObjectDataHolder(SeedReader(), TRAIN_PERCENT.toDouble(), true, false)

        val trainData = ArrayList<ClassifiedData>()
        val testData = ArrayList<ClassifiedData>()

        val filteredData = ArrayList<ClassifiedData>()
        for (data in dataHolder.data) {
            if (data.classId != 2) {
                filteredData.add(data)
            }
        }

        Collections.shuffle(filteredData)
        for (data in filteredData) {
            if (trainData.size * 100 < filteredData.size * TRAIN_PERCENT) {
                trainData.add(data)
            } else {
                testData.add(data)
            }
        }

        val logitBoost = LogitBoost { LogisticRegressor(dataHolder.vectorSize, 0.5, 10) }
        val classificationModel = ClassificationModel(logitBoost) { doubles -> Math.round(doubles[0]).toInt() }
        for (it in 0..99) {
            classificationModel.trainClassified(trainData)
        }

        for (data in testData) {
            System.err.printf("{%s} -> %.5f\n", data.classId, classificationModel.output(data)[0])
        }
    }
}
