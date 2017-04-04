package learning.regressors;

import learning.regressors.Regressor;

public interface RegressorFactory {
    Regressor createInstance();
}
