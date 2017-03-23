package classifying;

import data.ClassifiedData;
import data.Data;

import java.util.List;

public class WeightedRegressor implements Regressor {
    private final Regressor regressor;
    private double weight = 1;

    public WeightedRegressor(Regressor regressor) {
        this.regressor = regressor;
    }

    @Override
    public void train(List<ClassifiedData> data) {
        regressor.train(data);
    }

    @Override
    public double regress(Data data) {
        return weight * regressor.regress(data);
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
