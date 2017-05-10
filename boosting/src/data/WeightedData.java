package data;

public class WeightedData implements RegressionData {

    private double weight;
    private final RegressionData data;

    public WeightedData(RegressionData data) {
        this(data, 1);
    }

    public WeightedData(RegressionData data, double weight) {
        this.data = data;
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double[] asVector() {
        return data.asVector();
    }

    @Override
    public double output() {
        return data.output() * weight;
    }
}
