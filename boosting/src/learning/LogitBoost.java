package learning;

import data.Data;
import data.RegressionData;
import data.RegressionDataPoint;
import data.WeightedData;
import learning.model.Model;
import learning.regressors.Regressor;
import learning.regressors.RegressorFactory;
import learning.regressors.WeightedRegressor;

import java.util.ArrayList;
import java.util.List;

public class LogitBoost implements Model {

    private final int iterations;
    private final RegressorFactory regressorFactory;

    private final List<Regressor> weakLearners = new ArrayList<>();

    public LogitBoost(int iterations, RegressorFactory regressorFactory) {
        this.iterations = iterations;
        this.regressorFactory = regressorFactory;
    }

    @Override
    public void train(List<RegressionData> dataSet) {
        weakLearners.clear();
        for (int i = 0; i < iterations; i++) {
            List<RegressionData> regressionData = new ArrayList<>();

            for (RegressionData data : dataSet) {
                double p = p(data);
                double z = (data.output() - p) / (p * (1 - p));
                double w = p * (1 - p);
                regressionData.add(new WeightedData(new RegressionDataPoint(data, z), w));
            }

            WeightedRegressor regressor = new WeightedRegressor(createRegressor());
            regressor.train(regressionData);
            regressor.setWeight(0.5);
            weakLearners.add(regressor);

            if (i % 50 == 0) {
                System.err.println("Iteration = " + i);
            }
        }
    }

    @Override
    public double[] output(Data data) {
        return new double[] {
                p(data)
        };
    }

    private double F(Data data) {
        double sum = 0;
        for (Regressor regressor : weakLearners) {
            sum += regressor.regress(data);
        }
        return sum;
    }

    private double p(Data x) {
        double F = F(x);
        return Math.exp(F) / (Math.exp(F) + Math.exp(-F));
    }

    private Regressor createRegressor() {
        return regressorFactory.createInstance();
    }
}
