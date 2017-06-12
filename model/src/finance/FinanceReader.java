package finance;

import data.DataReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FinanceReader implements DataReader<Double> {

    private static final String fileName = "resources\\table.csv";
    private static final int ARGUMENTS_SIZE = 6;

    private final int valueIndex;

    public FinanceReader(int valueIndex) {
        this.valueIndex = valueIndex;
    }

    @Override
    public List<Double> readData() throws IOException {
        List<Double> resultData = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            input.readLine();

            String nextLine;
            while ((nextLine = input.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(nextLine, ",");
                resultData.add(readValueFromLine(tokenizer));
            }
        }
        return resultData;
    }

    private double readValueFromLine(StringTokenizer tokenizer) {
        tokenizer.nextToken();
        double[] values = new double[ARGUMENTS_SIZE];
        for (int i = 0; i < ARGUMENTS_SIZE; i++) {
            values[i] = Double.parseDouble(tokenizer.nextToken());
        }
        return values[valueIndex];
    }
}
