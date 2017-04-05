import data.ClassifiedData;
import data.DataHolder;
import data.RegressionData;
import data.RegressionDataDecorator;
import iris.Iris;
import iris.IrisReader;
import learning.regressors.LogisticRegressor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Runner {

    private static final int TRAIN_PERCENT = 60;

    public static void main(String[] args) {
        DataHolder<Iris> dataHolder = new DataHolder<>(new IrisReader());
        dataHolder.normalize();

        List<RegressionData> trainData = new ArrayList<>();
        List<ClassifiedData> testData = new ArrayList<>();

        List<ClassifiedData> filteredData = new ArrayList<>();
        for (ClassifiedData data : dataHolder.getData()) {
            if (data.getClassId() != 2) {
                filteredData.add(data);
            }
        }

        Collections.shuffle(filteredData);
        for (ClassifiedData data : filteredData) {
            if (trainData.size() < filteredData.size() * 60 / 100.0) {
                trainData.add(new RegressionDataDecorator(data));
            } else {
                testData.add(data);
            }
        }


        LogisticRegressor regressor = new LogisticRegressor(dataHolder.getVectorSize(), 0.1, 300);
        regressor.train(trainData);

        int correctly = 0;
        for (ClassifiedData data : testData) {
            double regress = regressor.regress(data);
            if (regress > 1 - regress) {
                if (data.getClassId() == 1) {
                    correctly++;
                }
            } else {
                if (data.getClassId() == 0) {
                    correctly++;
                }
            }
        }
        System.err.printf("orrectly = %.2f\n", 100.0 * correctly / testData.size());
    }
}
