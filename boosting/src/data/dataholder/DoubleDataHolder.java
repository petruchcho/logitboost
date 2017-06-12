package data.dataholder;

import data.DataReader;

public class DoubleDataHolder extends DataHolder<Double> {

    public DoubleDataHolder(DataReader<Double> dataReader, double trainPercent, boolean normalize, boolean shuffle) {
        super(dataReader, trainPercent, normalize, shuffle);
    }

    @Override
    protected void normalize() {
        if (data == null || data.isEmpty()) {
            return;
        }
        double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
        for (double x : data) {
            min = Math.min(x, min);
            max = Math.max(x, max);
        }
        for (int i = 0; i < data.size(); i++) {
            data.set(i, (data.get(i) - min) / (max - min));
        }
    }

    @Override
    public int getVectorSize() {
        return 1;
    }
}
