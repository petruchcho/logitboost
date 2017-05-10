package data;

public class RegressionDataPoint implements RegressionData {
    private final Data x;
    private final double y;

    public RegressionDataPoint(Data x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double[] asVector() {
        return x.asVector();
    }

    @Override
    public double output() {
        return y;
    }
}
