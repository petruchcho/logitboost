package learning.classification;

import data.ClassifiedData;
import data.Data;
import data.RegressionData;
import data.RegressionDataDecorator;
import learning.model.ModelWithTeacher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ClassificationModel implements Classifier {
    private final ModelWithTeacher model;
    private final Function<double[], Integer> outputToClass;

    public ClassificationModel(ModelWithTeacher model, Function<double[], Integer> outputToClass) {
        this.model = model;
        this.outputToClass = outputToClass;
    }

    @Override
    public int classify(Data data) {
        return outputToClass.apply(output(data));
    }

    public void trainClassified(List<ClassifiedData> data) {
        List<RegressionData> regressionData = new ArrayList<>();
        for (ClassifiedData classifiedData : data) {
            regressionData.add(new RegressionDataDecorator(classifiedData));
        }
        model.trainAll(regressionData);
    }

    @Override
    public void train(RegressionData data) {
        model.train(data);
    }

    @Override
    public void trainAll(List<? extends RegressionData> data) {
        throw new RuntimeException("Classification model should be trained on class-labeled data");
    }

    @Override
    public double[] output(Data data) {
        return model.output(data);
    }
}
