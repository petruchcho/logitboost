package learning.regressors;

import data.Data;
import data.RegressionData;

import java.util.List;

public interface Regressor {
    void train(List<RegressionData> data);

    double regress(Data data);
}
