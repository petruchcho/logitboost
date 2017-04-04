package learning;

import data.ClassifiedData;
import data.Data;
import learning.regressors.Regressor;
import learning.regressors.RegressorFactory;
import learning.regressors.WeightedRegressor;

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
