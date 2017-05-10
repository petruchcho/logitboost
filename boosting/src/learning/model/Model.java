package learning.model;

import data.Data;
import data.RegressionData;

import java.util.List;

public interface Model {
    void train(List<RegressionData> data);

    double[] output(Data data);
}
