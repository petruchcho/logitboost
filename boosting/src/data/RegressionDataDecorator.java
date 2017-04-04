package data;

public class RegressionDataDecorator implements RegressionData {
    private final ClassifiedData classifiedData;
    private Converter converter;

    public RegressionDataDecorator(ClassifiedData classifiedData) {
        this.classifiedData = classifiedData;
    }

    public RegressionDataDecorator(ClassifiedData classifiedData, Converter converter) {
        this(classifiedData);
        this.converter = converter;
    }

    @Override
    public double output() {
        if (converter == null) {
            return classifiedData.getClassId();
        }
        return converter.toOutput(classifiedData.getClassId());
    }

    @Override
    public double[] asVector() {
        return classifiedData.asVector();
    }

    public interface Converter {
        double toOutput(int classId);
    }
}
