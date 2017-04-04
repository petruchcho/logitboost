package learning.regressors;

import data.Data;
import data.RegressionData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Stump implements Regressor {
    private final int argId;

    /**
     * It is going to be 1 if v[argId] > t and -1 otherwise.
     */
    private double t;

    public Stump(int argId) {
        this.argId = argId;
        throw new NotImplementedException();
    }

    @Override
    public void train(List<RegressionData> data) {
        List<RegressionData> sortedData = new ArrayList<>(data);
        sortedData.sort(Comparator.comparingDouble(o -> o.asVector()[argId]));
        int firstClassCount = 0;
        for (RegressionData dataInstance : sortedData) {
            if (dataInstance.output() == 1) {
                firstClassCount++;
            }
        }
        t = sortedData.get(0).asVector()[argId] - 0.5;
        int bestCorrect = firstClassCount;
        int currentCorrect = firstClassCount;
        for (RegressionData dataInstance : sortedData) {
            if (dataInstance.output() == 1) {
                currentCorrect--;
            } else {
                currentCorrect++;
            }
            if (bestCorrect < currentCorrect) {
                bestCorrect = currentCorrect;
                t = dataInstance.asVector()[argId];
            }
        }
    }

    @Override
    public double regress(Data data) {
        return data.asVector()[argId] > t ? 1 : -1;
    }
}
