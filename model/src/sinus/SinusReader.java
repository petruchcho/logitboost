package sinus;

import data.DataReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SinusReader implements DataReader<Double> {

    private final double periodsCount;
    private final int size;

    public SinusReader(double periodsCount, int size) {
        this.periodsCount = periodsCount;
        this.size = size;
    }

    @Override
    public List<Double> readData() throws IOException {
        List<Double> resultData = new ArrayList<>();
        double step = Math.PI * 2 * periodsCount / size;
        for (double value = 0; resultData.size() < size; value += step) {
            resultData.add(Math.sin(value));
        }
        return resultData;
    }
}
