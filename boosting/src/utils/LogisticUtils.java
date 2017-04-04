package utils;

public class LogisticUtils {
    private LogisticUtils() {
    }

    public static double logisticFunction(double t) {
        return 1 / (1 + Math.exp(-t));
    }
}
