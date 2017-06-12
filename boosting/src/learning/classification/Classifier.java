package learning.classification;

import data.Data;
import learning.model.ModelWithTeacher;

public interface Classifier extends ModelWithTeacher {
    int classify(Data data);
}
