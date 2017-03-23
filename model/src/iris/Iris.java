package iris;

import data.ClassifiedData;

public class Iris implements ClassifiedData {

    enum IrisClass {
        SETOSA,
        VERSICOLOUR,
        VIRGINICA
    }

    private final double sepalLength;
    private final double sepalWidth;
    private final double petalLength;
    private final double petalWidth;
    private final IrisClass irisClass;

    private final double[] vector;

    public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth, IrisClass irisClass) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.irisClass = irisClass;

        vector = new double[]{
                sepalLength,
                sepalWidth,
                petalLength,
                petalWidth
        };
    }

    @Override
    public double[] asVector() {
        return vector;
    }

    @Override
    public int getClassId() {
        switch (irisClass) {
            case SETOSA:
                return 0;
            case VERSICOLOUR:
                return 1;
            case VIRGINICA:
                return 2;
            default:
                throw new RuntimeException("Unexpected class");
        }
    }
}
