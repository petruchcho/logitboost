package classifying;

import data.ClassifiedData;
import data.Data;

import java.util.List;

public interface Classifier {
    double[] classify(Data data);

    void train(List<ClassifiedData> dataSet);
}
