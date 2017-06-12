package data.dataholder;

import data.Data;
import data.DataReader;

public class ObjectDataHolder<T extends Data> extends DataHolder<T> {

    public ObjectDataHolder(DataReader<T> dataReader, double trainPercent, boolean normalize, boolean shuffle) {
        super(dataReader, trainPercent, normalize, shuffle);
    }

    @Override
    protected void normalize() {
        if (data == null || data.isEmpty()) {
            return;
        }
        int vectorSize = data.get(0).asVector().length;
        for (int i = 0; i < vectorSize; i++) {
            double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
            for (Data data : this.data) {
                min = Math.min(min, data.asVector()[i]);
                max = Math.max(max, data.asVector()[i]);
            }
            for (Data data : this.data) {
                double value = data.asVector()[i];
                data.asVector()[i] = (value - min) / (max - min);
            }
        }
    }

    @Override
    public int getVectorSize() {
        if (data == null || data.isEmpty()) {
            return 0;
        }
        return data.get(0).asVector().length;
    }
}
