package learning;

import data.*;
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
        return null;
    }

    public double F(Data data) {
        double sum = 0;
        for (Regressor regressor : weakLearners) {
            sum += regressor.regress(data);
        }
        return sum;
    }

    @Override
    public void train(List<ClassifiedData> dataSet) {
        weakLearners.clear();
        for (int i = 0; i < iterations; i++) {
            List<RegressionData> regressionData = new ArrayList<>();

            for (ClassifiedData data : dataSet) {
                double p = p(data);
                double z = (data.getClassId() - p) / (p * (1 - p));
                double w = p * (1 - p);
                regressionData.add(new WeightedData(new RegressionDataPoint(data, z), w));
            }

            WeightedRegressor regressor = new WeightedRegressor(createRegressor());
            regressor.train(regressionData);
            regressor.setWeight(0.5);
            weakLearners.add(regressor);

            System.out.printf("Iteration = %s, error = %.2f\n", i + 1, calculateError(dataSet));
        }
    }

    private double calculateError(List<ClassifiedData> dataSet) {
        int correct = 0;
        for (ClassifiedData data : dataSet) {
            if (data.getClassId() == classifyInternal(data)) {
                correct++;
            }
        }
        return 100.0 * correct / dataSet.size();
    }

    /**
     * 0 - 1
     */
    private int classifyInternal(Data x) {
        if (p(x) > 0.5) {
            return 1;
        } else {
            return 0;
        }
    }

    public double p(Data x) {
        double F = F(x);
        return Math.exp(F) / (Math.exp(F) + Math.exp(-F));
    }

    private Regressor createRegressor() {
        return regressorFactory.createInstance();
    }
}
