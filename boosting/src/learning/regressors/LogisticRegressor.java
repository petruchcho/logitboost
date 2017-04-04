package learning.regressors;

import data.ClassifiedData;
import data.Data;
import data.RegressionData;
import utils.LogisticUtils;
import utils.MatrixUtils;
import utils.RandomUtils;

import java.util.List;

public class LogisticRegressor implements Regressor {

    private final int inputSize;

    private final double learningStep;
    private final int iterations;

    private double[] c;

    public LogisticRegressor(int inputSize, double learningStep, int iterations) {
        this.inputSize = inputSize;
        this.learningStep = learningStep;
        this.iterations = iterations;

        this.c = new double[inputSize];
        for (int i = 0; i < inputSize; i++) {
            c[i] = RandomUtils.getInstance().nextDouble() - 0.5;
        }
    }

    @Override
    public void train(List<RegressionData> dataSet) {
        for (int iter = 0; iter < iterations; iter++) {
            double[] sum = new double[inputSize];
            for (RegressionData data : dataSet) {
                double[] x = data.asVector();
                double y = data.output();
                sum = MatrixUtils.add(sum, MatrixUtils.multiply(x, y - regress(data)));
            }
            this.c = MatrixUtils.add(c, MatrixUtils.multiply(sum, learningStep));
        }
    }

    /**
     * Shows probability that class = 1
     */
    @Override
    public double regress(Data data) {
        return f(MatrixUtils.multiply(c, data.asVector()));
    }

    private double f(double z) {
        return LogisticUtils.logisticFunction(z);
    }
}
