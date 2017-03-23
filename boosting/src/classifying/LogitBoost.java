package classifying;

import data.ClassifiedData;
import data.Data;

import java.util.ArrayList;
import java.util.List;

public class LogitBoost implements Classifier {

    private final int iterations;
    private final RegressorFactory regressorFactory;

    private final List<Regressor> weakLearners = new ArrayList<>();

    public LogitBoost(int iterations, RegressorFactory regressorFactory) {
        this.iterations = iterations;
        this.regressorFactory = regressorFactory;
    }

    @Override
    public double[] classify(Data dataInstance) {
        double probability = calculateProbability(dataInstance);
        return new double[]{
                1 - probability,
                probability
        };
    }

    @Override
    public void train(List<ClassifiedData> dataSet) {
        weakLearners.clear();
        for (int iteration = 0; iteration < iterations; iteration++) {
            for (ClassifiedData dataInstance : dataSet) {
                double probability = calculateProbability(dataInstance);
                double z;
                if (dataInstance.getClassId() == 1) {
                    z = Math.min(3, 1 / probability);
                } else {
                    z = Math.max(-3, -1 / (1 - probability));
                }
                double weight = Math.max(0, probability * (1 - probability));

            }
        }
    }

    private WeightedRegressor createWeightedRegressor() {
        return new WeightedRegressor(regressorFactory.createInstance());
    }

    private double calculateProbability(Data x) {
        double fx = F(x);
        double efx = Math.exp(fx);
        double enfx = Math.exp(-fx);
        if (Double.isInfinite(efx) && efx > 0 && enfx < 1e-15)//Well classified point could return a Infinity which turns into NaN
            return 1.0;
        return efx / (efx + enfx);
    }

    private double F(Data dataInstance) {
        double fx = 0.0; //0 so when we are uninitalized calculateProbability will return 0.5
        for (Regressor fm : weakLearners) {
            fx += fm.regress(dataInstance);
        }

        return fx * 0.5;
    }
}
