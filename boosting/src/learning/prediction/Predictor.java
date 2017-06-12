package learning.prediction;

import learning.model.ModelWithTeacher;

public interface Predictor extends ModelWithTeacher {
    double predictNext(double[] vector);
}
