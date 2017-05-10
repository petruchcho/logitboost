package data.dataholder;

import data.Data;
import data.DataReader;

public class ObjectDataHolder<T extends Data> extends DataHolder<T> {

    public ObjectDataHolder(DataReader<T> dataReader, int trainPercent, boolean shuffle) {
        super(dataReader, trainPercent, shuffle);
    }

    @Override
    public void normalize() {
        if (data == null || data.isEmpty()) {
            return;
        }
        int vectorSize = data.get(0).asVector().length;
        for (int i = 0; i < vectorSize; i++) {
            double sum = 0;
            for (Data data : this.data) {
                sum += data.asVector()[i] * data.asVector()[i];
            }
            sum = Math.sqrt(sum);
            for (Data data : this.data) {
                double value = data.asVector()[i];
                data.asVector()[i] = value / sum;
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
