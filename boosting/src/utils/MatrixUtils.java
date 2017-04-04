package utils;

import java.util.Arrays;

public class MatrixUtils {
    private MatrixUtils() {
    }

    public static double multiply(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException();
        }
        double res = 0;
        for (int i = 0; i < a.length; i++) {
            res += a[i] * b[i];
        }
        return res;
    }

    public static double[] multiply(double[] a, double coeff) {
        double[] res = Arrays.copyOf(a, a.length);
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] * coeff;
        }
        return res;
    }

    public static double[] subtract(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException();
        }
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] - b[i];
        }
        return res;
    }

    public static double[] add(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException();
        }
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] + b[i];
        }
        return res;
    }
}
