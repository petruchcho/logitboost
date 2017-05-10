package test;

import data.ClassifiedData;
import data.dataholder.DataHolder;
import learning.LogitBoost;
import learning.classification.ClassificationModel;
import learning.regressors.LogisticRegressor;
import seed.SeedReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogitBoostClassificationTest {

    private static final int TRAIN_PERCENT = 80;

    public static void main(String[] args) {
        DataHolder<? extends ClassifiedData> dataHolder = new DataHolder<>(new SeedReader(), trainPercent, shuffle);
        dataHolder.normalize();

        List<ClassifiedData> trainData = new ArrayList<>();
        List<ClassifiedData> testData = new ArrayList<>();

        List<ClassifiedData> filteredData = new ArrayList<>();
        for (ClassifiedData data : dataHolder.getData()) {
            if (data.getClassId() != 2) {
                filteredData.add(data);
            }
        }

        Collections.shuffle(filteredData);
        for (ClassifiedData data : filteredData) {
            if (trainData.size() * 100 < filteredData.size() * TRAIN_PERCENT) {
                trainData.add(data);
            } else {
                testData.add(data);
            }
        }

        LogitBoost logitBoost = new LogitBoost(1000, () -> new LogisticRegressor(dataHolder.getVectorSize(), 0.5, 10));
        ClassificationModel classificationModel = new ClassificationModel(logitBoost, doubles -> (int) Math.round(doubles[0]));
        classificationModel.trainClassified(trainData);

        for (ClassifiedData data : testData) {
            System.err.printf("{%s} -> %.5f\n", data.getClassId(), classificationModel.output(data)[0]);
        }
    }
}