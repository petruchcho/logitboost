package learning.classification;

import data.Data;
import learning.model.Model;

public interface Classifier extends Model {
    int classify(Data data);
}
