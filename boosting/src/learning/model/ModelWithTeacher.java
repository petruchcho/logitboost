package learning.model;

import data.Data;
import data.RegressionData;

import java.util.List;

public interface ModelWithTeacher {
    void train(RegressionData data);

    void trainAll(List<? extends RegressionData> data);

    double[] output(Data data);
}
