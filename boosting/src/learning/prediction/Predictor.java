package learning.prediction;

import data.Data;
import learning.model.Model;

public interface Predictor extends Model {
    double predict(Data data);
}
