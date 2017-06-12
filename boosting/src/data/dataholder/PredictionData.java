package data.dataholder;

import data.RegressionData;

public class PredictionData implements RegressionData {

    private final double[] window;
    private final double expectedResult;

    public PredictionData(double[] window, double expectedResult) {
        this.window = window;
        this.expectedResult = expectedResult;
    }

    @Override
    public double output() {
        return expectedResult;
    }

    @Override
    public double[] asVector() {
        return window;
    }
}
