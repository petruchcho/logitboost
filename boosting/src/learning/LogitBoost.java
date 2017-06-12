package learning;

import data.Data;
import data.RegressionData;
import data.RegressionDataPoint;
import data.WeightedData;
import learning.model.ModelWithTeacher;
import learning.regressors.Regressor;
import learning.regressors.RegressorFactory;
import learning.regressors.WeightedRegressor;

import java.util.ArrayList;
import java.util.List;

public class LogitBoost implements ModelWithTeacher {

    private final RegressorFactory regressorFactory;

    private final List<Regressor> weakLearners = new ArrayList<>();

    public LogitBoost(RegressorFactory regressorFactory) {
        this.regressorFactory = regressorFactory;
    }

    @Override
    public void trainAll(List<? extends RegressionData> dataSet) {
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
    }

    @Override
    public void train(RegressionData data) {
        throw new RuntimeException("Logitboost should know all data set");
    }

    @Override
    public double[] output(Data data) {
        return new double[]{
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
