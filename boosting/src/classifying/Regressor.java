package classifying;

import data.ClassifiedData;
import data.Data;

import java.util.List;

public interface Regressor {
    void train(List<ClassifiedData> data);

    double regress(Data data);
}
