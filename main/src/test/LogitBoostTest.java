package test;

import data.ClassifiedData;
import data.DataHolder;
import learning.LogitBoost;
import learning.regressors.LogisticRegressor;
import seed.SeedReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogitBoostTest {

    private static final int TRAIN_PERCENT = 100;

    public static void main(String[] args) {
        DataHolder<? extends ClassifiedData> dataHolder = new DataHolder<>(new SeedReader());
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

        LogitBoost logitBoost = new LogitBoost(1000, () -> new LogisticRegressor(dataHolder.getVectorSize(), 0.5, 200));
        logitBoost.train(trainData);

        for (ClassifiedData data : trainData) {
            System.err.printf("{%s} -> %.5f\n", data.getClassId(), logitBoost.p(data));
        }
    }
}
