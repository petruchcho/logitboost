package learning.prediction;

import data.Data;
import data.RegressionData;
import learning.model.ModelWithTeacher;

import java.util.List;

public class PredictionModel implements Predictor {
    private final ModelWithTeacher model;

    public PredictionModel(ModelWithTeacher model) {
        this.model = model;
    }

    @Override
    public void trainAll(List<? extends RegressionData> data) {
        model.trainAll(data);
    }

    @Override
    public void train(RegressionData data) {
        model.train(data);
    }

    @Override
    public double[] output(Data data) {
        return model.output(data);
    }

    public double predictNext(double[] vector) {
        return output(() -> vector)[0];
    }
}
