package data.dataholder;

import data.DataReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DataHolder<T> {

    private final DataReader<T> dataReader;
    protected List<T> data;
    private final int trainPercent;
    private final boolean shuffle;

    private List<T> trainData;
    private List<T> testData;

    public DataHolder(DataReader<T> dataReader, int trainPercent, boolean shuffle) {
        this.dataReader = dataReader;
        this.trainPercent = trainPercent;
        this.shuffle = shuffle;
        readData();
        splitData();
    }

    private void splitData() {
        trainData = new ArrayList<>();
        testData = new ArrayList<>();

        if (shuffle) {
            Collections.shuffle(data);
        }

        for (T data : this.data) {
            if (trainData.size() < this.data.size() * trainPercent / 100.0) {
                trainData.add(data);
            } else {
                testData.add(data);
            }
        }
    }

    private void readData() {
        try {
            data = dataReader.readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> getData() {
        return data;
    }

    public List<T> getTestData() {
        return testData;
    }

    public List<T> getTrainData() {
        return trainData;
    }

    public abstract void normalize();

    public abstract int getVectorSize();
}
